package bpv.utils.validationapi.rule.resolvers;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class ValidationMessageContextResolver {
    private static final Map<Annotation, ValidationMessageContext> MESSAGE_INTERPOLATOR_CONTEXT = new HashMap<Annotation, ValidationMessageContext>();

    private static ValidationMessageContext buildMessageInterpolationContext(Annotation constraint){
        ValidationMessageContext context = MESSAGE_INTERPOLATOR_CONTEXT.get(constraint);
        if(context == null){
            context = new ValidationMessageContext(constraint);
            MESSAGE_INTERPOLATOR_CONTEXT.put(constraint, context);
        }
        return context;
    }

    public static ValidationMessageContext resolve(Annotation constraint){
        return buildMessageInterpolationContext(constraint);
    }

}
