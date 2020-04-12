package bpv.utils.validationapi.rule;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ValidationRule {
    private final String code;
    private final String message;
    private final Set<MessageParam> params = new HashSet<>();

    public ValidationRule(String code) {
        this(code, null);
    }
    public ValidationRule(String code, String message){
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Set<MessageParam> getParams() {
        return params;
    }

    public void addParam(String name, Object value){
        params.add(new MessageParam(name, value));
    }

    @Override
    public String toString() {
        return "{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationRule that = (ValidationRule) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
