package ua.edu.ukma.objectanalysis.openvet.domain.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentStatusTest {

    @Test
    void testEnumValues() {
        assertEquals("PENDING", PaymentStatus.PENDING.name());
        assertEquals("PAID", PaymentStatus.PAID.name());
        assertEquals("CANCELLED", PaymentStatus.CANCELLED.name());
    }
}

