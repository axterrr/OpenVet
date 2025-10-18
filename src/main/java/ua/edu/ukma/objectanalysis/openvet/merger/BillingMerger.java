package ua.edu.ukma.objectanalysis.openvet.merger;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.billing.BillingEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.PaymentStatus;
import ua.edu.ukma.objectanalysis.openvet.dto.billing.BillingRequest;
import ua.edu.ukma.objectanalysis.openvet.repository.appointment.AppointmentRepository;

import static ua.edu.ukma.objectanalysis.openvet.merger.MergerUtils.ifNotNull;

@Component
@RequiredArgsConstructor
public class BillingMerger implements BaseMerger<BillingEntity, BillingRequest> {

    private final AppointmentRepository appointmentRepository;

    @Override
    public void mergeCreate(BillingEntity entity, BillingRequest request) {
        if (request == null || entity == null) { return; }
        commonMerge(entity, request);

        if (request.getPaymentStatus() == null) {
            entity.setPaymentStatus(PaymentStatus.PENDING);
        }

        if (request.getAppointmentId() != null) {
            entity.setAppointment(appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + request.getAppointmentId()))
            );
        }
    }

    @Override
    public void mergeUpdate(BillingEntity entity, BillingRequest request) {
        if (request == null || entity == null) { return; }
        commonMerge(entity, request);
    }

    private void commonMerge(BillingEntity entity, BillingRequest request) {
        ifNotNull(request.getAmount(), entity::setAmount);
        ifNotNull(request.getCurrency(), entity::setCurrency);
        ifNotNull(request.getPaymentMethod(), entity::setPaymentMethod);
        ifNotNull(request.getPaymentStatus(), entity::setPaymentStatus);
    }
}
