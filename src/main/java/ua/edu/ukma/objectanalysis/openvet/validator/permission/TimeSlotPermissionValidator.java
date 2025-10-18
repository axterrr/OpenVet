package ua.edu.ukma.objectanalysis.openvet.validator.permission;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.AppointmentEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.TimeSlotEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PetEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.UserEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.appointment.TimeSlotRequest;
import ua.edu.ukma.objectanalysis.openvet.repository.user.UserRepository;

@Component
@RequiredArgsConstructor
public class TimeSlotPermissionValidator extends BasePermissionValidator<TimeSlotEntity, TimeSlotRequest> {

    private final UserRepository userRepository;

    @Override
    public void validateForGetAll() {
        require(isAuthenticatedUserAdmin() || isAuthenticatedUserVeterinarian());
    }

    @Override
    public void validateForGet(TimeSlotEntity entity) {
        if (isAuthenticatedUserAdmin()) { return; }
        if (isAuthenticatedUserVeterinarian()) {
            require(entity.getVeterinarian() != null);
            require(entity.getVeterinarian().getEmail().equals(getAuthenticatedUserEmail()));
            return;
        }
        if (isAuthenticatedUserPetOwner()) {
            AppointmentEntity appointment = entity.getAppointment();
            PetEntity pet = appointment != null ? appointment.getPet() : null;
            require(pet != null && pet.getOwner().getEmail().equals(getAuthenticatedUserEmail()));
            return;
        }
        forbid();
    }

    @Override
    public void validateForCreate(TimeSlotRequest request) {
        if (isAuthenticatedUserAdmin()) { return; }
        if (isAuthenticatedUserVeterinarian()) {
            require(request.getVeterinarianId() != null);
            UserEntity me = getAuthenticatedUser();
            require(me != null && me.getId().equals(request.getVeterinarianId()));
            return;
        }
        forbid();
    }

    @Override
    public void validateForUpdate(TimeSlotEntity entity) {
        if (isAuthenticatedUserAdmin()) { return; }
        if (isAuthenticatedUserVeterinarian()) {
            require(entity.getVeterinarian() != null && entity.getVeterinarian().getEmail().equals(getAuthenticatedUserEmail()));
            return;
        }
        forbid();
    }

    @Override
    public void validateForDelete(TimeSlotEntity entity) {
        validateForUpdate(entity);
    }

    private UserEntity getAuthenticatedUser() {
        String email = getAuthenticatedUserEmail();
        if (email == null) { return null; }
        return userRepository.findByEmail(email).orElse(null);
    }

    public void validateForGenerate() {
        require(isAuthenticatedUserAdmin());
    }
}
