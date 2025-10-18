package ua.edu.ukma.objectanalysis.openvet.domain.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentMethodTest {

    @Test
    void testEnumValues() {
        assertEquals("CASH", PaymentMethod.CASH.name());
        assertEquals("CREDIT_CARD", PaymentMethod.CREDIT_CARD.name());
    }
}

