package bpv.utils.validationapi.data;



import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Map;
import java.util.Set;

public class Manager extends Person {
    @Valid
    private Set<Person> employees;

    @Min(1000)
    private double bonus;

    @Valid
    private Map<String,Person> leaderByGroup;
}
