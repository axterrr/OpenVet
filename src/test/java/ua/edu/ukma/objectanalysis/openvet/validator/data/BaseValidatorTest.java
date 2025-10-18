package ua.edu.ukma.objectanalysis.openvet.validator.data;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.ukma.objectanalysis.openvet.exception.ValidationException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaseValidatorTest {

    @Mock
    private Validator validator;

    @InjectMocks
    private ConcreteValidator baseValidator;

    private static class ConcreteValidator extends BaseValidator<Object, Object> {}

    private Object request;
    private Object entity;

    @BeforeEach
    void setUp() {
        request = new Object();
        entity = new Object();
    }

    @Test
    void validateForCreate_NoViolations_NoException() {
        when(validator.validate(request)).thenReturn(Collections.emptySet());
        assertDoesNotThrow(() -> baseValidator.validateForCreate(request));
    }

    @Test
    void validateForCreate_WithViolations_ThrowsValidationException() {
        Set<ConstraintViolation<Object>> violations = new HashSet<>();
        ConstraintViolation<Object> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Error message");
        violations.add(violation);

        when(validator.validate(request)).thenReturn(violations);
        assertThrows(ValidationException.class, () -> baseValidator.validateForCreate(request));
    }

    @Test
    void validateForUpdate_NoViolations_NoException() {
        when(validator.validate(request)).thenReturn(Collections.emptySet());
        assertDoesNotThrow(() -> baseValidator.validateForUpdate(request, entity));
    }

    @Test
    void validateForUpdate_WithViolations_NullInvalidValue_NoException() {
        Set<ConstraintViolation<Object>> violations = new HashSet<>();
        ConstraintViolation<Object> violation = mock(ConstraintViolation.class);
        when(violation.getInvalidValue()).thenReturn(null);
        violations.add(violation);

        when(validator.validate(request)).thenReturn(violations);
        assertDoesNotThrow(() -> baseValidator.validateForUpdate(request, entity));
    }

    @Test
    void validateForUpdate_WithViolations_NonNullInvalidValue_ThrowsValidationException() {
        Set<ConstraintViolation<Object>> violations = new HashSet<>();
        ConstraintViolation<Object> violation = mock(ConstraintViolation.class);
        when(violation.getInvalidValue()).thenReturn("invalid");
        when(violation.getMessage()).thenReturn("Error message");
        violations.add(violation);

        when(validator.validate(request)).thenReturn(violations);
        assertThrows(ValidationException.class, () -> baseValidator.validateForUpdate(request, entity));
    }

    @Test
    void validateForDelete_NoException() {
        assertDoesNotThrow(() -> baseValidator.validateForDelete(entity));
    }
}

