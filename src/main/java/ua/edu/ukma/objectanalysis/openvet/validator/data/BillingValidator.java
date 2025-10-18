package ua.edu.ukma.objectanalysis.openvet.validator.data;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.billing.BillingEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.PaymentStatus;
import ua.edu.ukma.objectanalysis.openvet.dto.billing.BillingRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.ConflictException;

@Component
@RequiredArgsConstructor
public class BillingValidator extends BaseValidator<BillingEntity, BillingRequest> {

    @Override
    public void validateForUpdate(BillingRequest request, BillingEntity entity) {
        if (entity.getPaymentStatus() != PaymentStatus.PENDING) {
            throw new ConflictException("Billing cannot be updated");
        }
        super.validateForUpdate(request, entity);
    }
}
