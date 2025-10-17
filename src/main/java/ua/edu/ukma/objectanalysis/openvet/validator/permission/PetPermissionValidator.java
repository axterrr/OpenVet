package ua.edu.ukma.objectanalysis.openvet.validator.permission;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PetEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.UserEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;
import ua.edu.ukma.objectanalysis.openvet.dto.pet.PetRequest;
import ua.edu.ukma.objectanalysis.openvet.repository.user.UserRepository;

@Component
@RequiredArgsConstructor
public class PetPermissionValidator extends BasePermissionValidator<PetEntity, PetRequest> {

    private final UserRepository userRepository;

    @Override
    public void validateForGetAll() {
        UserRole role = getAuthenticatedUserRole();
        require(role == UserRole.ADMIN || role == UserRole.VETERINARIAN);
    }

    @Override
    public void validateForGet(PetEntity entity) {
        if (isAuthenticatedUserAdmin() || isAuthenticatedUserVeterinarian()) {
            return;
        }
        require(isOwnedByAuthenticatedUser(entity) || isPendingForAuthenticatedUser(entity));
    }

    @Override
    public void validateForCreate(PetRequest request) {
        if (isAuthenticatedUserAdmin() || isAuthenticatedUserVeterinarian()) {
            return;
        }
        UserEntity me = getAuthenticatedUser();
        require(me != null && me.getPhoneNumber().equals(request.getOwnerPhone()));
    }

    @Override
    public void validateForUpdate(PetEntity entity) {
        if (isAuthenticatedUserAdmin() || isAuthenticatedUserVeterinarian()) {
            return;
        }
        require(isOwnedByAuthenticatedUser(entity));
    }

    @Override
    public void validateForDelete(PetEntity entity) {
        if (isAuthenticatedUserAdmin()) {
            return;
        }
        require(isOwnedByAuthenticatedUser(entity));
    }

    public void validateForOwnershipChange(PetEntity entity) {
        require(isAuthenticatedUserAdmin() || isAuthenticatedUserVeterinarian());
    }

    public void validateForSetPendingOwner(PetEntity entity) {
        if (isAuthenticatedUserAdmin() || isAuthenticatedUserVeterinarian()) { return; }
        require(isOwnedByAuthenticatedUser(entity));
    }

    public void validateForAddVaccinationRecord(PetEntity entity) {
        requireUserRole(UserRole.VETERINARIAN);
    }

    private boolean isOwnedByAuthenticatedUser(PetEntity entity) {
        String email = getAuthenticatedUserEmail();
        return entity.getOwner() != null && email != null && email.equals(entity.getOwner().getEmail());
    }

    private boolean isPendingForAuthenticatedUser(PetEntity entity) {
        UserEntity me = getAuthenticatedUser();
        return entity.getPendingOwner() != null && me != null && me.getPhoneNumber().equals(entity.getPendingOwner().getPhoneNumber());
    }

    private UserEntity getAuthenticatedUser() {
        String email = getAuthenticatedUserEmail();
        if (email == null) { return null; }
        return userRepository.findByEmail(email).orElse(null);
    }
}
