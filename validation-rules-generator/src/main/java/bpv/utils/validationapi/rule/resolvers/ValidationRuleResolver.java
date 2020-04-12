package bpv.utils.validationapi.rule.resolvers;

import bpv.utils.validationapi.rule.ValidationRule;
import bpv.utils.validationapi.rule.data.PropertyMetaData;

import java.lang.annotation.Annotation;
import java.util.*;

public class ValidationRuleResolver {

    private static final Map<Annotation,ValidationRule> VALIDATION_RULE_CACHE = new HashMap<>();

    private static Set<ValidationRule> buildAndCacheValidationRules(PropertyMetaData property){
        Set<ValidationRule> rules = new HashSet<>();
        for(Annotation constraint : property.getConstraintAnnotations()){
            ValidationRule rule = VALIDATION_RULE_CACHE.get(constraint);
            if(rule == null){
                rule = ValidationRuleBuilder.build(property, constraint);
                VALIDATION_RULE_CACHE.put(constraint, rule);
            }
            rules.add(rule);
        }
        return rules;

    }

    public static Set<ValidationRule> resolve(PropertyMetaData property){
        return ValidationRuleResolver.buildAndCacheValidationRules(property);
    }
}
