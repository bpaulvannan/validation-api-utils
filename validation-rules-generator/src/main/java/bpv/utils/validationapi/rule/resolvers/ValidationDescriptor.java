package bpv.utils.validationapi.rule.resolvers;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.Payload;
import javax.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ValidationDescriptor<T extends Annotation> implements ConstraintDescriptor {

    public static final String ATTR_MESSAGE = "message";
    private static final String ATTR_GROUPS = "groups";
    private static String ATTR_PAYLOAD = "payload";
    private static String ATTR_VALIDATED_BY = "validatedBy";

    private final T constraint;
    private Map<String,Object> attributes;


    private ValidationDescriptor(T constraint){
        if(constraint.annotationType().isAnnotationPresent(Constraint.class)){
            this.constraint = constraint;
            this.attributes = resolveAttributes(constraint);
        }else{
            throw new IllegalArgumentException("Validation constraint is required, but provided " + constraint.annotationType().getName());
        }
    }

    @Override
    public Annotation getAnnotation() {
        return constraint;
    }

    @Override
    public Set<Class<?>> getGroups() {
        return (Set<Class<?>>) attributes.get(ATTR_GROUPS);
    }

    @Override
    public Set<Class<? extends Payload>> getPayload() {
        return (Set<Class<? extends Payload>>) attributes.get(ATTR_PAYLOAD);
    }

    @Override
    public List<Class<? extends ConstraintValidator>> getConstraintValidatorClasses() {
        return (List<Class<? extends ConstraintValidator>>) attributes.get(ATTR_VALIDATED_BY);
    }

    @Override
    public Map<String, Object> getAttributes() {
        Map<String,Object> attrs = new HashMap<String,Object>();
        attrs.put(ATTR_MESSAGE, attributes.get(ATTR_MESSAGE));
        return attrs;
    }

    public String getMessageKey(){
        return (String)attributes.get(ATTR_MESSAGE);
    }

    public Map<String, Object> getMessageParams() {
        Map<String,Object> attrs = new HashMap<String,Object>();
        attrs.putAll(attributes);
        attrs.remove(ATTR_MESSAGE);
        attrs.remove(ATTR_GROUPS);
        attrs.remove(ATTR_PAYLOAD);
        attrs.remove(ATTR_VALIDATED_BY);
        return attrs;
    }

    @Override
    public Set<ConstraintDescriptor<?>> getComposingConstraints() {
        return null;
    }

    @Override
    public boolean isReportAsSingleViolation() {
        return false;
    }

    private Map<String,Object> resolveAttributes(T annotation){
        Map<String,Object> tmpAttributes = new HashMap<String,Object>();
        for(Method method : annotation.annotationType().getDeclaredMethods()){
            String attrName = method.getName();
            Object attrValue = null;
            try{
                attrValue =  method.invoke(annotation);
            } catch (IllegalArgumentException|IllegalAccessException|InvocationTargetException e) {
                //Ignore. should not happen
            }
            tmpAttributes.put(attrName, attrValue);
        }
        return tmpAttributes;
    }

    static ValidationDescriptor fromConstraint(Annotation annotation){
        if(annotation.annotationType().isAnnotationPresent(Constraint.class)){
            return new ValidationDescriptor(annotation);
        }
        return null;
    }
}
