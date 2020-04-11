package bpv.utils.validationapi.data;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Person {

    @Valid
    private Department department = new Department();

    @NotNull
    private String name;

    @Min(20)
    @Max(60)
    private int age;

    @Min(500)
    private double salary;

    private String hobby;
}
