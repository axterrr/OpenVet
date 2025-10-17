package ua.edu.ukma.objectanalysis.openvet.validator.permission;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.AppointmentEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.examination.MedicalRecordsEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;
import ua.edu.ukma.objectanalysis.openvet.dto.examination.MedicalRecordsRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.NotFoundException;
import ua.edu.ukma.objectanalysis.openvet.repository.appointment.AppointmentRepository;

@Component
@RequiredArgsConstructor
public class MedicalRecordsPermissionValidator extends BasePermissionValidator<MedicalRecordsEntity, MedicalRecordsRequest> {

    private final AppointmentRepository appointmentRepository;

    @Override
    public void validateForGetAll() {
        requireUserRole(UserRole.ADMIN);
    }

    @Override
    public void validateForGet(MedicalRecordsEntity entity) {
        if (isAuthenticatedUserAdmin()) {
            return;
        }
        if (isAuthenticatedUserVeterinarian()) {
            requireUserEmail(entity.getAppointment().getTimeSlot().getVeterinarian().getEmail());
        }
        if (isAuthenticatedUserPetOwner()) {
            requireUserEmail(entity.getAppointment().getPet().getOwner().getEmail());
        }
    }

    @Override
    public void validateForCreate(MedicalRecordsRequest request) {
        AppointmentEntity appointment = appointmentRepository.findById(request.getAppointmentId())
            .orElseThrow(() -> new NotFoundException("Appointment not found: " + request.getAppointmentId()));
        requireUserEmail(appointment.getTimeSlot().getVeterinarian().getEmail());
    }

    @Override
    public void validateForUpdate(MedicalRecordsEntity entity) {
        requireUserEmail(entity.getAppointment().getTimeSlot().getVeterinarian().getEmail());
    }

    @Override
    public void validateForDelete(MedicalRecordsEntity entity) {
        requireUserRole(UserRole.ADMIN);
    }
}
