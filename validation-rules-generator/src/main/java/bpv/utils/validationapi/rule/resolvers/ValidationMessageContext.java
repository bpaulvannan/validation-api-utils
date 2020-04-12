package bpv.utils.validationapi.rule.resolvers;

import javax.validation.Constraint;
import javax.validation.MessageInterpolator;
import java.lang.annotation.Annotation;

public class ValidationMessageContext implements MessageInterpolator.Context {

    private final ValidationDescriptor<? extends Annotation> constraintDescriptor;

    public ValidationMessageContext(Annotation constraint){
        if(constraint.annotationType().isAnnotationPresent(Constraint.class)){
            constraintDescriptor = new ValidationDescriptor(constraint);
        }
        throw new IllegalArgumentException("Validation constraint is required, but provided " + constraint.annotationType().getName());
    }

    @Override
    public ValidationDescriptor<?> getConstraintDescriptor() {
        return constraintDescriptor;
    }

    @Override
    public Object getValidatedValue() {
        return null;
    }
}
