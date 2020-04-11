package bpv.utils.validationapi.rule;

import bpv.utils.validationapi.rule.data.PropertyMetaData;
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
            String name = property.getName();
            Class<?> type = property.getType();
            if(property.hasValidations()){
                if(Map.class.isAssignableFrom(type)){
                    ruleDescriptors.addAll(buildRuleDescriptor(rootPath + name + "[{key}].", resolveGenericType(property.getGenericType(),1)));
                }else if(Collection.class.isAssignableFrom(type)){
                    ruleDescriptors.addAll(buildRuleDescriptor(rootPath + name + "[{position}].", resolveGenericType(property.getGenericType(),0)));
                }else {
                    ruleDescriptors.addAll(buildRuleDescriptor( rootPath + name + ".", type));
                }
            }
            if(property.hasConstraints()){
                Set<ValidationRule> validationRules = ValidationRuleResolver.resolve(property);
                if(validationRules.size() > 0){
                    RuleDescriptor rule = new RuleDescriptor();
                    rule.setPath(rootPath + name);
                    rule.setRules(validationRules);
                    ruleDescriptors.add(rule);
                }
            }
        }
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
