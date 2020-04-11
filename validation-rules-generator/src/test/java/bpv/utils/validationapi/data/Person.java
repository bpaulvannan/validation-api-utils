package bpv.utils.validationapi.data;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Test class with fields primitive, object, object array and no validations
 */
public class Person {

    /**
     * Another object to validate
     */
    @Valid
    private Department department;

    /**
     * Constraint on String
     */
    @NotNull
    private String name;

    /**
     * Multiple constratints
     */
    @Min(20)
    @Max(60)
    private int age;

    /**
     * Constraint on primitive field
     */
    @Min(500)
    private double salary;

    /**
     * No validation
     */
    private String hobby;

    /**
     * Constraints on an array + Another object to validate
     */
    @Max(3)
    @Valid
    private Project[] projects;
}
