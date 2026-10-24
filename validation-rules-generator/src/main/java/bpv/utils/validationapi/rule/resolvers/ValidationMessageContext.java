package bpv.utils.validationapi.rule.resolvers;

import jakarta.validation.Constraint;
import jakarta.validation.MessageInterpolator;
import java.lang.annotation.Annotation;

public class ValidationMessageContext implements MessageInterpolator.Context {

    private final ValidationDescriptor<? extends Annotation> constraintDescriptor;

    ValidationMessageContext(Annotation constraint){
        if(constraint.annotationType().isAnnotationPresent(Constraint.class)){
            constraintDescriptor = new ValidationDescriptor(constraint);
        }else{
            throw new IllegalArgumentException("Validation constraint is required, but provided " + constraint.annotationType().getName());
        }
    }

    @Override
    public ValidationDescriptor<?> getConstraintDescriptor() {
        return constraintDescriptor;
    }

    @Override
    public Object getValidatedValue() {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> type) {
        return null;
    }
}
