package bpv.utils.validationapi.rule.resolvers;

import bpv.utils.validationapi.data.Person;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("ValidationMessageContext")
class ValidationMessageContextTest {

    @Test
    @DisplayName("constructor creates constraint descriptor from valid constraint")
    void constructorCreatesDescriptor() throws Exception {
        Field field = Person.class.getDeclaredField("name");
        Annotation annotation = field.getAnnotation(NotNull.class);

        ValidationMessageContext context = new ValidationMessageContext(annotation);

        assertNotNull(context.getConstraintDescriptor());
    }

    @Test
    @DisplayName("constructor throws IllegalArgumentException for non-Constraint annotation")
    void constructorThrowsForNonConstraint() throws Exception {
        // SimpleMarker is not @Constraint-annotated
        Annotation annotation = MarkerHolder.class.getAnnotation(SimpleMarker.class);
        assertNotNull(annotation);

        assertThrows(IllegalArgumentException.class, () -> new ValidationMessageContext(annotation));
    }

    @Test
    @DisplayName("getValidatedValue returns null")
    void getValidatedValueReturnsNull() throws Exception {
        Field field = Person.class.getDeclaredField("name");
        Annotation annotation = field.getAnnotation(NotNull.class);
        ValidationMessageContext context = new ValidationMessageContext(annotation);

        assertNull(context.getValidatedValue());
    }

    @Test
    @DisplayName("unwrap returns null")
    void unwrapReturnsNull() throws Exception {
        Field field = Person.class.getDeclaredField("name");
        Annotation annotation = field.getAnnotation(NotNull.class);
        ValidationMessageContext context = new ValidationMessageContext(annotation);

        assertNull(context.unwrap(String.class));
    }

    @Test
    @DisplayName("getConstraintDescriptor returns non-null descriptor")
    void getConstraintDescriptor() throws Exception {
        Field field = Person.class.getDeclaredField("name");
        Annotation annotation = field.getAnnotation(NotNull.class);
        ValidationMessageContext context = new ValidationMessageContext(annotation);

        assertAll("constraint descriptor",
            () -> assertNotNull(context.getConstraintDescriptor()),
            () -> assertNotNull(context.getConstraintDescriptor().getAnnotation())
        );
    }
}
