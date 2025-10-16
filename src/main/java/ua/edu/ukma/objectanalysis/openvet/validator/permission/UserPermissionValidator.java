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
        if (isAuthenticated() && !isAuthenticatedUserAdmin()) {
            forbid();
        }
        require(request.getRole() == UserRole.PET_OWNER);
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
        if (isAuthenticatedUserVeterinarian()) {
            return;
        }
        requireUserEmail(entity.getEmail());
    }
}
