package bpv.utils.validationapi.rule.data;

import bpv.utils.validationapi.data.Department;
import bpv.utils.validationapi.data.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("PropertyMetaData")
class PropertyMetaDataTest {

    @Test
    @DisplayName("fromField creates metadata with correct name and type")
    void fromField() throws Exception {
        Field field = Person.class.getDeclaredField("name");
        PropertyMetaData meta = PropertyMetaData.fromField(field);

        assertAll("fromField",
            () -> assertEquals("name", meta.getName()),
            () -> assertEquals(String.class, meta.getType()),
            () -> assertTrue(meta.isField()),
            () -> assertFalse(meta.isMethod())
        );
    }

    @Test
    @DisplayName("fromMethod resolves bean property name via Introspector")
    void fromMethod() throws Exception {
        Method method = Department.class.getMethod("getName");
        PropertyMetaData meta = PropertyMetaData.fromMethod(method);

        assertAll("fromMethod",
            () -> assertEquals("name", meta.getName()),
            () -> assertEquals(String.class, meta.getType()),
            () -> assertTrue(meta.isMethod()),
            () -> assertFalse(meta.isField())
        );
    }

    @Test
    @DisplayName("fromMethod returns null when bean info cannot be resolved")
    void fromMethodUnresolvable() throws Exception {
        Method method = String.class.getMethod("length");
        PropertyMetaData meta = PropertyMetaData.fromMethod(method);

        assertNull(meta.getName());
    }

    @Test
    @DisplayName("hasConstraints detects constraint annotations")
    void hasConstraints() throws Exception {
        PropertyMetaData nameMeta = PropertyMetaData.fromField(Person.class.getDeclaredField("name"));
        PropertyMetaData hobbyMeta = PropertyMetaData.fromField(Person.class.getDeclaredField("hobby"));

        assertTrue(nameMeta.hasConstraints());
        assertFalse(hobbyMeta.hasConstraints());
    }

    @Test
    @DisplayName("hasValidations detects @Valid annotation")
    void hasValidations() throws Exception {
        PropertyMetaData departmentMeta = PropertyMetaData.fromField(Person.class.getDeclaredField("department"));
        PropertyMetaData nameMeta = PropertyMetaData.fromField(Person.class.getDeclaredField("name"));

        assertTrue(departmentMeta.hasValidations());
        assertFalse(nameMeta.hasValidations());
    }

    @Test
    @DisplayName("canProcessProperty returns true when constraints or validations present")
    void canProcessProperty() throws Exception {
        PropertyMetaData nameMeta = PropertyMetaData.fromField(Person.class.getDeclaredField("name"));
        PropertyMetaData departmentMeta = PropertyMetaData.fromField(Person.class.getDeclaredField("department"));
        PropertyMetaData hobbyMeta = PropertyMetaData.fromField(Person.class.getDeclaredField("hobby"));

        assertTrue(nameMeta.canProcessProperty());
        assertTrue(departmentMeta.canProcessProperty());
        assertFalse(hobbyMeta.canProcessProperty());
    }

    @Test
    @DisplayName("constraintAnnotations contains only constraint annotations")
    void constraintAnnotations() throws Exception {
        Field ageField = Person.class.getDeclaredField("age");
        PropertyMetaData meta = PropertyMetaData.fromField(ageField);

        assertEquals(2, meta.getConstraintAnnotations().size());
    }

    @Test
    @DisplayName("equals and hashCode for same field are equal")
    void equalsAndHashCode() throws Exception {
        Field f1 = Person.class.getDeclaredField("name");
        Field f2 = Person.class.getDeclaredField("name");
        PropertyMetaData a = PropertyMetaData.fromField(f1);
        PropertyMetaData b = PropertyMetaData.fromField(f2);

        assertAll("equals/hashCode",
            () -> assertEquals(a, b),
            () -> assertEquals(a.hashCode(), b.hashCode())
        );
    }

    @Test
    @DisplayName("equals with null and different class")
    void equalsWithNullAndDifferentClass() throws Exception {
        PropertyMetaData meta = PropertyMetaData.fromField(Person.class.getDeclaredField("name"));

        assertNotEquals(meta, null);
        assertNotEquals(meta, "not metadata");
    }

    @Test
    @DisplayName("getGenericType returns generic type of field")
    void getGenericType() throws Exception {
        Field field = Person.class.getDeclaredField("department");
        PropertyMetaData meta = PropertyMetaData.fromField(field);

        assertEquals(Department.class, meta.getGenericType());
    }
}
