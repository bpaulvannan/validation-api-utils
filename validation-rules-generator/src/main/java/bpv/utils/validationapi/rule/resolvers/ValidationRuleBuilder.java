package bpv.utils.validationapi.rule.resolvers;

import bpv.utils.validationapi.rule.data.ValidationRule;
import bpv.utils.validationapi.rule.data.PropertyMetaData;

import jakarta.validation.MessageInterpolator;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.AssertTrue;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import java.lang.annotation.Annotation;
import java.util.Map;

public class ValidationRuleBuilder {

    private static final MessageInterpolator MESSAGE_INTERPOLATOR = new ParameterMessageInterpolator();

    private static boolean isAssertConstraint(Annotation constraint){
        return constraint.annotationType().equals(AssertTrue.class) || constraint.annotationType().equals(AssertFalse.class);
    }

    private static String getCode(PropertyMetaData property, Annotation constraint){
        if(property.isMethod() && isAssertConstraint(constraint)){
            return ValidationCodeResolver.resolve(property);
        }else{
            return ValidationCodeResolver.resolve(constraint);
        }
    }

    public static ValidationRule build(PropertyMetaData property, Annotation constraint){

        ValidationMessageContext context = ValidationMessageContextResolver.resolve(constraint);
        ValidationDescriptor descriptor = context.getConstraintDescriptor();
        String key = descriptor.getMessageKey();


        String code = getCode(property, constraint);
        String message = MESSAGE_INTERPOLATOR.interpolate(key, context);
        ValidationRule rule = new ValidationRule(code, message);
        for(Map.Entry<String,Object> entry : ((Map<String,Object>)descriptor.getMessageParams()).entrySet()){
            rule.addParam(entry.getKey(), entry.getValue());
        }

        return rule;
    }

}
