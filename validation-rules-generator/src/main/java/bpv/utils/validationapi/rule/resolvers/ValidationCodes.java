package bpv.utils.validationapi.rule.resolvers;

public enum ValidationCodes {
    NOT_NULL("REQUIRED"), PATTERN("FORMAT"), SIZE("RANGE");

    private final String value;
    ValidationCodes(String value){
        this.value = value;
    }

    public String value(){
        return value;
    }

    public static String toValue(String code){
        try{
            return ValidationCodes.valueOf(code).value();
        }catch (IllegalArgumentException e){
            return code;
        }
    }
}
