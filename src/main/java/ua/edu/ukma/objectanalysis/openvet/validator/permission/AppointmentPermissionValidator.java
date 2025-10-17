package ua.edu.ukma.objectanalysis.openvet.validator.permission;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.AppointmentEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PetEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.appointment.AppointmentRequest;
import ua.edu.ukma.objectanalysis.openvet.repository.pet.PetRepository;
import ua.edu.ukma.objectanalysis.openvet.repository.user.UserRepository;

@Component
@RequiredArgsConstructor
public class AppointmentPermissionValidator extends BasePermissionValidator<AppointmentEntity, AppointmentRequest> {

    private final PetRepository petRepository;

    @Override
    public void validateForGetAll() {
        require(isAuthenticatedUserAdmin() || isAuthenticatedUserVeterinarian());
    }

    @Override
    public void validateForGet(AppointmentEntity entity) {
        if (isAuthenticatedUserAdmin() || isAuthenticatedUserVeterinarian()) { return; }
        require(isOwnedByAuthenticatedUser(entity.getPet()));
    }

    @Override
    public void validateForCreate(AppointmentRequest request) {
        if (isAuthenticatedUserAdmin() || isAuthenticatedUserVeterinarian()) { return; }
        PetEntity pet = petRepository.findById(request.getPetId()).orElse(null);
        require(isOwnedByAuthenticatedUser(pet));
    }

    @Override
    public void validateForUpdate(AppointmentEntity entity) {
        if (isAuthenticatedUserAdmin()) { return; }
        if (isAuthenticatedUserVeterinarian()) {
            require(entity.getTimeSlot() != null &&
                entity.getTimeSlot().getVeterinarian() != null &&
                entity.getTimeSlot().getVeterinarian().getEmail().equals(getAuthenticatedUserEmail()));
            return;
        }
        require(isOwnedByAuthenticatedUser(entity.getPet()));
    }

    @Override
    public void validateForDelete(AppointmentEntity entity) {
        validateForUpdate(entity);
    }

    private boolean isOwnedByAuthenticatedUser(PetEntity pet) {
        String email = getAuthenticatedUserEmail();
        return pet != null && pet.getOwner() != null && email != null && email.equals(pet.getOwner().getEmail());
    }
}

