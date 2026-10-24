package bpv.utils.validationapi.data;



import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import java.util.Map;
import java.util.Set;

/**
 * Class to test validations from super class
 */
public class Manager extends Person {
    /**
     * Validate set of objects
     */
    @Valid
    private Set<Person> employees;

    @Min(1000)
    private double bonus;

    /**
     * Validate key value pairs
     */
    @Valid
    private Map<String,Person> leaderByGroup;

    @AssertTrue(message = "{MinProjectLeads}")
    public boolean isMinProjectLeads(){
        //some logic to check minimum leads for total managed employess
        return true;
    }
}
