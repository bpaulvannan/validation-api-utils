package bpv.utils.validationapi.rule;

import java.util.Objects;

public class ValidationAttribute {
    private final String name;
    private final Object value;
    public ValidationAttribute(String name, Object value){
        this.name = name;
        this.value = value;
    }
    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationAttribute that = (ValidationAttribute) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "{" + name + ":" + value + "}";
    }
}
