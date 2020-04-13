package bpv.utils.validationapi.rule.data;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class RuleDescriptor {
    private final String path;
    private final Set<ValidationRule> rules = new HashSet<>();

    public RuleDescriptor(String path){
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public Set<ValidationRule> getRules() {
        return rules;
    }

    public void addRules(Set<ValidationRule> rules) {
        this.rules.addAll(rules);
    }

    @Override
    public String toString() {
        return "RuleDescriptor{" +
                "path='" + path + '\'' +
                ", rules=" + rules +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleDescriptor that = (RuleDescriptor) o;
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
