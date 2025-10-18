package ua.edu.ukma.objectanalysis.openvet.dto.billing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.PaymentMethod;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.PaymentStatus;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillingResponse {
    private Long id;
    private BigDecimal amount;
    private String currency;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private Long appointmentId;
}
