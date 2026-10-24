package bpv.utils.validationapi.rule.data;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("MessageParam")
class MessageParamTest {

    @Test
    @DisplayName("constructor sets name and value")
    void constructorSetsNameAndValue() {
        MessageParam param = new MessageParam("min", 10);

        assertAll("constructor",
            () -> assertEquals("min", param.getName()),
            () -> assertEquals(10, param.getValue())
        );
    }

    @Test
    @DisplayName("constructor with null value")
    void constructorWithNullValue() {
        MessageParam param = new MessageParam("pattern", null);

        assertAll("null value",
            () -> assertEquals("pattern", param.getName()),
            () -> assertNull(param.getValue())
        );
    }

    @Test
    @DisplayName("equals compares by name only")
    void equalsByNameOnly() {
        MessageParam a = new MessageParam("min", 10);
        MessageParam b = new MessageParam("min", 20);
        MessageParam c = new MessageParam("max", 10);

        assertEquals(a, b);
        assertNotEquals(a, c);
    }

    @Test
    @DisplayName("equals with null and different class")
    void equalsWithNullAndDifferentClass() {
        MessageParam param = new MessageParam("min", 10);

        assertNotEquals(param, null);
        assertNotEquals(param, "not a message param");
    }

    @Test
    @DisplayName("hashCode is consistent with equals")
    void hashCodeConsistent() {
        MessageParam a = new MessageParam("min", 10);
        MessageParam b = new MessageParam("min", 20);

        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    @DisplayName("toString returns name:value format")
    void toStringFormat() {
        MessageParam param = new MessageParam("min", 10);

        assertEquals("{min:10}", param.toString());
    }
}
