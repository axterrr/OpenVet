package ua.edu.ukma.objectanalysis.openvet.dto.billing;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class BillingRequest {

    @NotNull(message = "Appointment id is required")
    private Long appointmentId;

    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount must be greater than or equal to zero")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    @Size(max = 3, message = "Currency must be at most 3 characters")
    private String currency;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;
}
