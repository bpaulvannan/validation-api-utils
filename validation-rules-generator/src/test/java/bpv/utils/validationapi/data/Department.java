package bpv.utils.validationapi.data;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Test class with constraints at both field and getter method
 */
public class Department {

    /**
     * Constraint at field
     */
    @Min(10)
    private String name;

    /**
     * Constraint at getter method
     * @return
     */
    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
