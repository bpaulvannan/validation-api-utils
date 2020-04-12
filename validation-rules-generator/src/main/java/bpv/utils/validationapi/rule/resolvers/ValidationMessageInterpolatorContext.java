package bpv.utils.validationapi.rule.resolvers;

import javax.validation.Constraint;
import javax.validation.MessageInterpolator;
import javax.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;

public class ValidationMessageInterpolatorContext implements MessageInterpolator.Context {

    private final ValidationDescriptor<? extends Annotation> constraintDescriptor;

    private ValidationMessageInterpolatorContext(Annotation constraint){
        if(constraint.annotationType().isAnnotationPresent(Constraint.class)){
            constraintDescriptor = ValidationDescriptor.fromConstraint(constraint);
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

    static ValidationMessageInterpolatorContext fromConstraint(Annotation annotation){
        if(annotation.annotationType().isAnnotationPresent(Constraint.class)){
            return new ValidationMessageInterpolatorContext(annotation);
        }
        return null;
    }
}
