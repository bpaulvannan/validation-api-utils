package bpv.utils.validationapi.rule;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ValidationRule {
    private final String code;
    private final String desc;
    private final Set<ValidationAttribute> attrs = new HashSet<>();

    public ValidationRule(String code) {
        this(code, null);
    }
    public ValidationRule(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public Set<ValidationAttribute> getAttrs() {
        return attrs;
    }

    public void addAttribute(String name, Object value){
        attrs.add(new ValidationAttribute(name, value));
    }

    @Override
    public String toString() {
        return "{" +
                "code='" + code + '\'' +
                ", desc='" + desc + '\'' +
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
