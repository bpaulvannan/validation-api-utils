package bpv.utils.validationapi.rule;

import java.util.Objects;
import java.util.Set;

public class RuleDescriptor {
    private String path;
    private Set<ValidationRule> rules;

    public RuleDescriptor(){
    }

    public RuleDescriptor(String path){
        this.path = path;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Set<ValidationRule> getRules() {
        return rules;
    }

    public void setRules(Set<ValidationRule> rules) {
        this.rules = rules;
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
