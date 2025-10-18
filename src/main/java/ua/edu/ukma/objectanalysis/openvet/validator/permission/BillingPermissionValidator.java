package ua.edu.ukma.objectanalysis.openvet.validator.permission;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.AppointmentEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.billing.BillingEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.PetOwnerEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;
import ua.edu.ukma.objectanalysis.openvet.dto.billing.BillingRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.NotFoundException;
import ua.edu.ukma.objectanalysis.openvet.repository.appointment.AppointmentRepository;
import ua.edu.ukma.objectanalysis.openvet.repository.user.PetOwnerRepository;

@Component
@RequiredArgsConstructor
public class BillingPermissionValidator extends BasePermissionValidator<BillingEntity, BillingRequest> {

    private final AppointmentRepository appointmentRepository;
    private final PetOwnerRepository petOwnerRepository;

    @Override
    public void validateForGetAll() {
        requireUserRole(UserRole.ADMIN);
    }

    @Override
    public void validateForGet(BillingEntity entity) {
        if (isAuthenticatedUserAdmin() || isAuthenticatedUserVeterinarian()) {
            return;
        }
        requireUserEmail(entity.getAppointment().getPet().getOwner().getEmail());
    }

    @Override
    public void validateForCreate(BillingRequest request) {
        if (isAuthenticatedUserAdmin()) { return; }
        AppointmentEntity appointment = appointmentRepository.findById(request.getAppointmentId())
            .orElseThrow(() -> new NotFoundException("Appointment not found"));
        requireUserEmail(appointment.getTimeSlot().getVeterinarian().getEmail());
    }

    @Override
    public void validateForUpdate(BillingEntity entity) {
        if (isAuthenticatedUserAdmin()) { return; }
        requireUserEmail(entity.getAppointment().getTimeSlot().getVeterinarian().getEmail());
    }

    @Override
    public void validateForDelete(BillingEntity entity) {
        requireUserRole(UserRole.ADMIN);
    }

    public void validateForGetByPetOwner(Long ownerId) {
        PetOwnerEntity owner = petOwnerRepository.findById(ownerId)
            .orElseThrow(() -> new NotFoundException("Owner not found"));
        if (isAuthenticatedUserAdmin()) { return; }
        requireUserEmail(owner.getEmail());
    }
}
