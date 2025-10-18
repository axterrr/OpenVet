package ua.edu.ukma.objectanalysis.openvet.domain.entity.billing;

import org.junit.jupiter.api.Test;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.AppointmentEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.PaymentMethod;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.PaymentStatus;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BillingEntityTest {

    @Test
    void testBillingEntity() {
        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setId(1L);

        BillingEntity billing = new BillingEntity();
        billing.setId(1L);
        billing.setAmount(new BigDecimal("100.00"));
        billing.setCurrency("USD");
        billing.setPaymentMethod(PaymentMethod.CASH);
        billing.setPaymentStatus(PaymentStatus.PAID);
        billing.setAppointment(appointment);

        assertNotNull(billing);
        assertEquals(1L, billing.getId());
        assertEquals(new BigDecimal("100.00"), billing.getAmount());
        assertEquals("USD", billing.getCurrency());
        assertEquals(PaymentMethod.CASH, billing.getPaymentMethod());
        assertEquals(PaymentStatus.PAID, billing.getPaymentStatus());
        assertEquals(appointment, billing.getAppointment());
    }
}

