package bpv.utils.validationapi.data;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Department {

    @Min(10)
    private String name;

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
