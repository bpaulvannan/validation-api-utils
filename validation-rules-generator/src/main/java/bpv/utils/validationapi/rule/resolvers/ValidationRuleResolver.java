package bpv.utils.validationapi.rule.resolvers;

import bpv.utils.validationapi.rule.ValidationRule;
import bpv.utils.validationapi.rule.data.PropertyMetaData;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import java.lang.annotation.Annotation;
import java.util.*;

public class ValidationRuleResolver {

    private static final Map<Annotation,ValidationRule> VALIDATION_RULE_CACHE = new HashMap<>();
    private static final Map<PropertyMetaData,Set<ValidationRule>> VALIDATIONS_BY_PROPERTY_CACHE = new HashMap<>();

    private static void buildAndCacheValidationRules(PropertyMetaData property){
        Set<ValidationRule> rules = VALIDATIONS_BY_PROPERTY_CACHE.get(property);
        if(rules == null){
            rules = new HashSet<>();
            VALIDATIONS_BY_PROPERTY_CACHE.put(property, rules);
        }
        for(Annotation constraint : property.getConstraintAnnotations()){
            ValidationRule rule = VALIDATION_RULE_CACHE.get(constraint);
            if(rule == null){
                rule = buildValidationRule(property, constraint);
                VALIDATION_RULE_CACHE.put(constraint, rule);
            }
            rules.add(rule);
        }

    }

    private static ValidationRule buildValidationRule(PropertyMetaData property, Annotation constraint){
        String code = null;
        if(property.isMethod() && isAssertConstraint(constraint)){
            code = ValidationCodeResolver.resolve(property.getName());
        }else{
            code = ValidationCodeResolver.resolve(constraint);
        }
        String desc = ValidationMessageResolver.resolve(constraint);
        return new ValidationRule(code, desc);
    }

    private static boolean isAssertConstraint(Annotation constraint){
        return constraint.annotationType().equals(AssertTrue.class) || constraint.annotationType().equals(AssertFalse.class);
    }


    public static Set<ValidationRule> resolve(PropertyMetaData property){
        ValidationRuleResolver.buildAndCacheValidationRules(property);
        return VALIDATIONS_BY_PROPERTY_CACHE.get(property);
    }
}
