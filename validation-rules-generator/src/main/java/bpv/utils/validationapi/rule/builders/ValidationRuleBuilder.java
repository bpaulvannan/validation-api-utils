package bpv.utils.validationapi.rule.builders;

import bpv.utils.validationapi.rule.ValidationRule;
import bpv.utils.validationapi.rule.resolvers.ValidationCodeResolver;
import bpv.utils.validationapi.rule.resolvers.ValidationMessageResolver;

import java.lang.annotation.Annotation;

public class ValidationRuleBuilder {

    public static ValidationRule build(Annotation constraint){
        String code = ValidationCodeResolver.resolve(constraint);
        String desc = ValidationMessageResolver.resolve(constraint);
        return new ValidationRule(code, desc);
    }
}
