package bpv.utils.validationapi.rule.resolvers;

import org.apache.commons.lang3.StringUtils;

import javax.validation.Constraint;
import java.lang.annotation.Annotation;


public final class ValidationCodeResolver {

    private ValidationCodeResolver(){}

    public static String resolve(Annotation constraint){
        return resolve(constraint.annotationType());
    }
    public static String resolve(Class<?> clazz){
        String[] nameParts = StringUtils.splitByCharacterTypeCamelCase(clazz.getSimpleName());
        String code = StringUtils.upperCase(StringUtils.joinWith("_", nameParts));
        if(clazz.isAnnotationPresent(Constraint.class)){
            try{
                return ValidationErrorCodes.valueOf(code).value();
            }catch (IllegalArgumentException e){
                return "INVALID_" + code;
            }
        }else if(Throwable.class.isAssignableFrom(clazz)){
            return "INVALID_" + code;
        }
        return null;
    }
}