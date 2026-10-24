package bpv.utils.validationapi.rule.resolvers;

import bpv.utils.validationapi.data.Person;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Constraint(validatedBy = {})
@interface TestConstraint {
    String message() default "test message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

@SimpleMarker("marker value")
class MarkerHolder {
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface SimpleMarker {
    String value() default "";
}

@DisplayName("ValidationDescriptor")
class ValidationDescriptorTest {

    @Test
    @DisplayName("constructor stores constraint annotation")
    void constructorStoresConstraint() throws Exception {
        Field field = Person.class.getDeclaredField("name");
        Annotation annotation = field.getAnnotation(NotNull.class);

        ValidationDescriptor descriptor = new ValidationDescriptor(annotation);

        assertNotNull(descriptor.getAnnotation());
    }

    @Test
    @DisplayName("constructor throws IllegalArgumentException for non-Constraint annotation")
    void constructorThrowsForNonConstraint() throws Exception {
        Annotation annotation = MarkerHolder.class.getAnnotation(SimpleMarker.class);

        assertThrows(IllegalArgumentException.class, () -> new ValidationDescriptor(annotation));
    }

    @Test
    @DisplayName("getMessageKey returns the message attribute")
    void getMessageKey() throws Exception {
        Field field = Person.class.getDeclaredField("name");
        Annotation annotation = field.getAnnotation(NotNull.class);
        ValidationDescriptor descriptor = new ValidationDescriptor(annotation);

        String messageKey = descriptor.getMessageKey();

        assertNotNull(messageKey);
    }

    @Test
    @DisplayName("getMessageParams excludes standard constraint attributes")
    void getMessageParams() throws Exception {
        Field field = Person.class.getDeclaredField("name");
        Annotation annotation = field.getAnnotation(NotNull.class);
        ValidationDescriptor descriptor = new ValidationDescriptor(annotation);

        Map<String, Object> params = descriptor.getMessageParams();

        assertAll("message params",
            () -> assertFalse(params.containsKey("message")),
            () -> assertFalse(params.containsKey("groups")),
            () -> assertFalse(params.containsKey("payload")),
            () -> assertFalse(params.containsKey("validatedBy"))
        );
    }

    @Test
    @DisplayName("getAttributes returns only the message attribute")
    void getAttributes() throws Exception {
        Field field = Person.class.getDeclaredField("name");
        Annotation annotation = field.getAnnotation(NotNull.class);
        ValidationDescriptor descriptor = new ValidationDescriptor(annotation);

        Map<String, Object> attrs = descriptor.getAttributes();

        assertTrue(attrs.containsKey("message"));
        assertFalse(attrs.containsKey("groups"));
        assertFalse(attrs.containsKey("payload"));
    }

    @Test
    @DisplayName("getMessageTemplate returns null")
    void getMessageTemplateReturnsNull() throws Exception {
        Field field = Person.class.getDeclaredField("name");
        Annotation annotation = field.getAnnotation(NotNull.class);
        ValidationDescriptor descriptor = new ValidationDescriptor(annotation);

        assertNull(descriptor.getMessageTemplate());
    }

    @Test
    @DisplayName("getGroups throws ClassCastException because stored groups is an array")
    void getGroupsThrowsClassCastException() throws Exception {
        Field field = Person.class.getDeclaredField("name");
        Annotation annotation = field.getAnnotation(NotNull.class);
        ValidationDescriptor descriptor = new ValidationDescriptor(annotation);

        assertThrows(ClassCastException.class, descriptor::getGroups);
    }

    @Test
    @DisplayName("getPayload throws ClassCastException because stored payload is an array")
    void getPayloadThrowsClassCastException() throws Exception {
        Field field = Person.class.getDeclaredField("name");
        Annotation annotation = field.getAnnotation(NotNull.class);
        ValidationDescriptor descriptor = new ValidationDescriptor(annotation);

        assertThrows(ClassCastException.class, descriptor::getPayload);
    }

    @Test
    @DisplayName("getValidationAppliesTo returns null")
    void getValidationAppliesToReturnsNull() throws Exception {
        Field field = Person.class.getDeclaredField("name");
        Annotation annotation = field.getAnnotation(NotNull.class);
        ValidationDescriptor descriptor = new ValidationDescriptor(annotation);

        assertNull(descriptor.getValidationAppliesTo());
    }

    @Test
    @DisplayName("getConstraintValidatorClasses returns null when no validatedBy attribute")
    void getConstraintValidatorClassesReturnsNull() throws Exception {
        Field field = Person.class.getDeclaredField("name");
        Annotation annotation = field.getAnnotation(NotNull.class);
        ValidationDescriptor descriptor = new ValidationDescriptor(annotation);

        assertNull(descriptor.getConstraintValidatorClasses());
    }

    @Test
    @DisplayName("getComposingConstraints returns null")
    void getComposingConstraintsReturnsNull() throws Exception {
        Field field = Person.class.getDeclaredField("name");
        Annotation annotation = field.getAnnotation(NotNull.class);
        ValidationDescriptor descriptor = new ValidationDescriptor(annotation);

        assertNull(descriptor.getComposingConstraints());
    }

    @Test
    @DisplayName("isReportAsSingleViolation returns false")
    void isReportAsSingleViolationReturnsFalse() throws Exception {
        Field field = Person.class.getDeclaredField("name");
        Annotation annotation = field.getAnnotation(NotNull.class);
        ValidationDescriptor descriptor = new ValidationDescriptor(annotation);

        assertFalse(descriptor.isReportAsSingleViolation());
    }

    @Test
    @DisplayName("getValueUnwrapping returns null")
    void getValueUnwrappingReturnsNull() throws Exception {
        Field field = Person.class.getDeclaredField("name");
        Annotation annotation = field.getAnnotation(NotNull.class);
        ValidationDescriptor descriptor = new ValidationDescriptor(annotation);

        assertNull(descriptor.getValueUnwrapping());
    }

    @Test
    @DisplayName("unwrap returns null")
    void unwrapReturnsNull() throws Exception {
        Field field = Person.class.getDeclaredField("name");
        Annotation annotation = field.getAnnotation(NotNull.class);
        ValidationDescriptor descriptor = new ValidationDescriptor(annotation);

        assertNull(descriptor.unwrap(String.class));
    }
}
