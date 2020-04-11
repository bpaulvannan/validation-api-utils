package bpv.utils.validationapi.rule.resolvers;

public enum ValidationErrorCodes {
    NOT_NULL("REQUIRED"), PATTERN("FORMAT"), SIZE("INVALID_MIN_MAX"), OPTIONAL("OPTIONAL");

    private final String value;
    ValidationErrorCodes(String value){
        this.value = value;
    }

    public String value(){
        return value;
    }
}
