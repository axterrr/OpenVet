package ua.edu.ukma.objectanalysis.openvet.validator.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.billing.BillingEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.PaymentStatus;
import ua.edu.ukma.objectanalysis.openvet.dto.billing.BillingRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.ConflictException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class BillingValidatorTest {

    @Mock
    private jakarta.validation.Validator validator;

    @InjectMocks
    private BillingValidator billingValidator;

    private BillingRequest billingRequest;
    private BillingEntity billingEntity;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        billingRequest = new BillingRequest();
        billingEntity = new BillingEntity();
        Field validatorField = BaseValidator.class.getDeclaredField("validator");
        validatorField.setAccessible(true);
        validatorField.set(billingValidator, validator);
    }

    @Test
    void validateForUpdate_PaymentStatusPending_NoException() {
        when(validator.validate(any())).thenReturn(Collections.emptySet());
        billingEntity.setPaymentStatus(PaymentStatus.PENDING);
        assertDoesNotThrow(() -> billingValidator.validateForUpdate(billingRequest, billingEntity));
    }

    @Test
    void validateForUpdate_PaymentStatusPaid_ThrowsConflictException() {
        billingEntity.setPaymentStatus(PaymentStatus.PAID);
        assertThrows(ConflictException.class, () -> billingValidator.validateForUpdate(billingRequest, billingEntity));
    }

    @Test
    void validateForUpdate_PaymentStatusCancelled_ThrowsConflictException() {
        billingEntity.setPaymentStatus(PaymentStatus.CANCELLED);
        assertThrows(ConflictException.class, () -> billingValidator.validateForUpdate(billingRequest, billingEntity));
    }
}
