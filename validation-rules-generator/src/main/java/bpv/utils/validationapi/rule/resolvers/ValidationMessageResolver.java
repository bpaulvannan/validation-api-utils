package bpv.utils.validationapi.rule.resolvers;

import javax.validation.MessageInterpolator;
import javax.validation.Validation;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class ValidationMessageResolver {

    private static final MessageInterpolator MESSAGE_INTERPOLATOR = Validation.buildDefaultValidatorFactory().getMessageInterpolator();
    private static final Map<Annotation,ValidationMessageInterpolatorContext> MESSAGE_INTERPOLATOR_CONTEXT = new HashMap<Annotation, ValidationMessageInterpolatorContext>();

    private static ValidationMessageInterpolatorContext buildMessageInterpolationContext(Annotation constraint){
        ValidationMessageInterpolatorContext context = MESSAGE_INTERPOLATOR_CONTEXT.get(constraint);
        if(context == null){
            context = ValidationMessageInterpolatorContext.fromConstraint(constraint);
            MESSAGE_INTERPOLATOR_CONTEXT.put(constraint, context);
        }
        return context;
    }

    public static String resolve(Annotation constraint){
        ValidationMessageInterpolatorContext context = buildMessageInterpolationContext(constraint);
        String key = (String)context.getConstraintDescriptor().getAttributes().get(ValidationDescriptor.ATTR_MESSAGE);
        return MESSAGE_INTERPOLATOR.interpolate(key,context);
    }

}
