package ua.edu.ukma.objectanalysis.openvet.validator.permission;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.UserEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;
import ua.edu.ukma.objectanalysis.openvet.dto.user.UserRequest;

@Component
@RequiredArgsConstructor
public class UserPermissionValidator extends BasePermissionValidator<UserEntity, UserRequest> {

    @Override
    public void validateForGetAll() {
        requireUserRole(UserRole.ADMIN);
    }

    @Override
    public void validateForGet(UserEntity entity) {
        if (entity.getEmail().equals(getAuthenticatedUserEmail()) || isAuthenticatedUserAdmin()) {
            return;
        }
        if (isAuthenticatedUserPetOwner()) {
            require(entity.getRole() == UserRole.VETERINARIAN);
        }
        if (isAuthenticatedUserVeterinarian()) {
            forbid();
        }
    }

    @Override
    public void validateForCreate(UserRequest request) {
        // Unauthenticated users can only self-register as PET_OWNER
        if (!isAuthenticated()) {
            require(request != null && request.getRole() == UserRole.PET_OWNER);
            return;
        }
        // Authenticated non-admin cannot create users
        if (!isAuthenticatedUserAdmin()) { forbid(); }
        // Admin can create veterinarians (and optionally other roles)
        require(request != null && (request.getRole() == UserRole.VETERINARIAN
            || request.getRole() == UserRole.PET_OWNER
            || request.getRole() == UserRole.ADMIN));
    }

    @Override
    public void validateForUpdate(UserEntity entity) {
        if (isAuthenticatedUserAdmin()) {
            require(entity.getRole() == UserRole.VETERINARIAN);
        }
        requireUserEmail(entity.getEmail());
    }

    @Override
    public void validateForDelete(UserEntity entity) {
        if (isAuthenticatedUserAdmin()) {
            return;
        }
        requireUserEmail(entity.getEmail());
    }

    public void validateForGetByRole(UserRole role) {
        requireUserRole(UserRole.ADMIN);
    }
}
