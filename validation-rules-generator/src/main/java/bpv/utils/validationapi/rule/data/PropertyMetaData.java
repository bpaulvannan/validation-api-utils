package bpv.utils.validationapi.rule.data;

import javax.validation.Constraint;
import javax.validation.Valid;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class PropertyMetaData {
    private final String name;
    private final AccessibleObject property;
    private final Class<?> type;
    private final Type genericType;
    private final Set<Annotation> constraints;

    private PropertyMetaData(String name, AccessibleObject property, Class<?> type, Type genericType){
        this.name = name;
        this.property = property;
        this.type = type;
        this.genericType = genericType;
        this.constraints = findConstraints(property.getAnnotations());
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public Type getGenericType() {
        return genericType;
    }

    public Set<Annotation> getConstraintAnnotations(){
        return constraints;
    }

    public boolean hasConstraints() {
        return !constraints.isEmpty();
    }

    public boolean hasValidations() {
        return this.property.isAnnotationPresent(Valid.class);
    }

    public boolean canProcessProperty(){
        return hasValidations() || hasConstraints();
    }

    public boolean isMethod(){
        return property instanceof Method;
    }

    public boolean isField(){
        return property instanceof Field;
    }

    private Set<Annotation> findConstraints(Annotation[] annotations){
        return Arrays.stream(annotations).filter((ann -> isConstraintAnnotation(ann))).collect(Collectors.toSet());
    }

    private boolean isConstraintAnnotation(Annotation ann){
        return ann.annotationType().isAnnotationPresent(Constraint.class);
    }

    public static PropertyMetaData fromField(Field field){
        return new PropertyMetaData(field.getName(), field, field.getType(), field.getGenericType());
    }

    public static PropertyMetaData fromMethod(Method method){
        String name = getFieldName(method);
        return new PropertyMetaData(name, method, method.getReturnType(), method.getGenericReturnType());
    }

    private static String getFieldName(Method method) {
        try
        {
            Class<?> clazz=method.getDeclaringClass();
            BeanInfo info = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] props = info.getPropertyDescriptors();
            for (PropertyDescriptor pd : props){
                if(method.equals(pd.getReadMethod())){
                    return pd.getName();
                }
            }
        } catch (Exception e){
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyMetaData that = (PropertyMetaData) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(type, that.type) &&
                Objects.equals(genericType, that.genericType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, genericType);
    }
}
