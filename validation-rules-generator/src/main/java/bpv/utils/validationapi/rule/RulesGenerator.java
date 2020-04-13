package bpv.utils.validationapi.rule;

import bpv.utils.validationapi.rule.data.PropertyMetaData;
import bpv.utils.validationapi.rule.data.RuleDescriptor;
import bpv.utils.validationapi.rule.data.ValidationRule;
import bpv.utils.validationapi.rule.resolvers.ValidationRuleResolver;

import java.lang.reflect.*;
import java.util.*;

public final class RulesGenerator {

    public static Set<RuleDescriptor> generate(Class<?> clazz){
        return buildRuleDescriptor("", clazz);
    }

    private static Set<RuleDescriptor> buildRuleDescriptor(String rootPath, Class<?> clazz) {
        Set<RuleDescriptor> ruleDescriptors = new LinkedHashSet<>();
        while(clazz != null && !Object.class.equals(clazz)){

            for(Field field : clazz.getDeclaredFields()){
                PropertyMetaData property = PropertyMetaData.fromField(field);
                processProperty(ruleDescriptors, rootPath, property);
            }

            for(Method method : clazz.getDeclaredMethods()){
                PropertyMetaData property = PropertyMetaData.fromMethod(method);
                processProperty(ruleDescriptors, rootPath, property);
            }

            clazz = clazz.getSuperclass();
        }

        return ruleDescriptors;

    }

    private static void processProperty(Set<RuleDescriptor> ruleDescriptors, String rootPath, PropertyMetaData property){
        if(property.canProcessProperty()){
            String path = rootPath + property.getName();
            Class<?> type = property.getType();
            if(property.hasValidations()){
                if(Map.class.isAssignableFrom(type)){
                    ruleDescriptors.addAll(buildRuleDescriptor(path + "[{key}].", resolveGenericType(property.getGenericType(),1)));
                }else if(Collection.class.isAssignableFrom(type)){
                    ruleDescriptors.addAll(buildRuleDescriptor(path + "[{position}].", resolveGenericType(property.getGenericType(),0)));
                }else if(type.isArray()){
                    ruleDescriptors.addAll(buildRuleDescriptor(path + "[{position}].", type.getComponentType()));
                }else {
                    ruleDescriptors.addAll(buildRuleDescriptor( path + ".", type));
                }
            }
            if(property.hasConstraints()){
                Set<ValidationRule> validationRules = ValidationRuleResolver.resolve(property);
                if(validationRules.size() > 0){
                    RuleDescriptor rule = getOrCreateRuleDescriptor(ruleDescriptors, path);
                    rule.addRules(validationRules);
                    ruleDescriptors.add(rule);
                }
            }
        }
    }

    private static RuleDescriptor getOrCreateRuleDescriptor(Set<RuleDescriptor> ruleDescriptors, String path){
        final RuleDescriptor ruleToFind = new RuleDescriptor(path);
        RuleDescriptor result = ruleDescriptors.stream().filter(ruleToFind::equals).findAny().orElse(new RuleDescriptor(path));
        return result;
    }

    private static Class<?> resolveGenericType(Type type, int position){
        if(type instanceof ParameterizedType){
            Type[] actualTypes = ((ParameterizedType) type).getActualTypeArguments();
            if(position >= 0 && actualTypes.length > position){
                return (Class<?>) actualTypes[position];
            }
        }
        return null;
    }


}
