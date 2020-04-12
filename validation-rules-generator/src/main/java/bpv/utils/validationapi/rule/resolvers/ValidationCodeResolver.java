package bpv.utils.validationapi.rule.resolvers;

import bpv.utils.validationapi.rule.data.PropertyMetaData;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;


public final class ValidationCodeResolver {

    private ValidationCodeResolver(){}

    public static String resolve(Annotation constraint){
        return resolve(constraint.annotationType());
    }

    public static String resolve(Class<?> clazz){
        String code = toCode(clazz.getSimpleName());
        return ValidationCodes.toValue(code);
    }

    public static String resolve(PropertyMetaData property){
        return toCode(property.getName());
    }

    private static String toCode(String name){
        String[] nameParts = StringUtils.splitByCharacterTypeCamelCase(name);
        return StringUtils.upperCase(StringUtils.joinWith("_", nameParts));
    }
}
