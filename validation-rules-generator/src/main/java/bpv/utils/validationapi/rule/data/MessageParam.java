package bpv.utils.validationapi.rule.data;

import java.util.Objects;

public class MessageParam {
    private final String name;
    private final Object value;
    MessageParam(String name, Object value){
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
        MessageParam that = (MessageParam) o;
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
