package ua.edu.ukma.objectanalysis.openvet.validator.permission;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.PetOwnerEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.UserEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.VeterinarianEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;
import ua.edu.ukma.objectanalysis.openvet.dto.user.UserRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.ForbiddenException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class UserPermissionValidatorTest {

    @InjectMocks
    private UserPermissionValidator userPermissionValidator;

    private UserEntity userEntity;
    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("user@example.com");
        userEntity.setRole(UserRole.PET_OWNER);

        userRequest = new UserRequest();
        userRequest.setRole(UserRole.PET_OWNER);

        userPermissionValidator = new UserPermissionValidator() {
            @Override
            protected boolean isAuthenticated() {
                return isAuthenticated;
            }

            @Override
            protected boolean isAuthenticatedUserAdmin() {
                return "ADMIN".equals(role);
            }

            @Override
            protected boolean isAuthenticatedUserVeterinarian() {
                return "VETERINARIAN".equals(role);
            }

            @Override
            protected boolean isAuthenticatedUserPetOwner() {
                return "PET_OWNER".equals(role);
            }

            @Override
            protected String getAuthenticatedUserEmail() {
                return email;
            }

            @Override
            protected UserRole getAuthenticatedUserRole() {
                if (role == null) return null;
                return UserRole.valueOf(role);
            }
        };
    }

    private String role;
    private String email;
    private boolean isAuthenticated;

    private void setupAuthentication(String role, String email, boolean isAuthenticated) {
        this.role = role;
        this.email = email;
        this.isAuthenticated = isAuthenticated;
    }

    @Test
    void validateForGetAll_AsAdmin_NoException() {
        setupAuthentication("ADMIN", "admin@example.com", true);
        assertDoesNotThrow(() -> userPermissionValidator.validateForGetAll());
    }

    @Test
    void validateForGetAll_AsVet_ThrowsForbiddenException() {
        setupAuthentication("VETERINARIAN", "vet@example.com", true);
        assertThrows(ForbiddenException.class, () -> userPermissionValidator.validateForGetAll());
    }

    @Test
    void validateForGet_Self_NoException() {
        setupAuthentication("PET_OWNER", "user@example.com", true);
        assertDoesNotThrow(() -> userPermissionValidator.validateForGet(userEntity));
    }

    @Test
    void validateForGet_AdminOnAnyUser_NoException() {
        setupAuthentication("ADMIN", "admin@example.com", true);
        assertDoesNotThrow(() -> userPermissionValidator.validateForGet(userEntity));
    }

    @Test
    void validateForGet_OwnerOnVet_NoException() {
        userEntity.setRole(UserRole.VETERINARIAN);
        setupAuthentication("PET_OWNER", "owner@example.com", true);
        assertDoesNotThrow(() -> userPermissionValidator.validateForGet(userEntity));
    }

    @Test
    void validateForGet_VetOnAnyUser_ThrowsForbiddenException() {
        setupAuthentication("VETERINARIAN", "vet@example.com", true);
        assertThrows(ForbiddenException.class, () -> userPermissionValidator.validateForGet(userEntity));
    }

    @Test
    void validateForCreate_UnauthenticatedAsOwner_NoException() {
        setupAuthentication(null, null, false);
        assertDoesNotThrow(() -> userPermissionValidator.validateForCreate(userRequest));
    }

    @Test
    void validateForCreate_UnauthenticatedAsVet_ThrowsForbiddenException() {
        setupAuthentication(null, null, false);
        userRequest.setRole(UserRole.VETERINARIAN);
        assertThrows(ForbiddenException.class, () -> userPermissionValidator.validateForCreate(userRequest));
    }

    @Test
    void validateForCreate_AdminAsVet_NoException() {
        setupAuthentication("ADMIN", "admin@example.com", true);
        userRequest.setRole(UserRole.VETERINARIAN);
        assertDoesNotThrow(() -> userPermissionValidator.validateForCreate(userRequest));
    }

    @Test
    void validateForUpdate_Self_NoException() {
        setupAuthentication("PET_OWNER", "user@example.com", true);
        assertDoesNotThrow(() -> userPermissionValidator.validateForUpdate(userEntity));
    }

    @Test
    void validateForUpdate_AdminOnVet_NoException() {
        userEntity.setRole(UserRole.VETERINARIAN);
        setupAuthentication("ADMIN", "admin@example.com", true);
        assertDoesNotThrow(() -> userPermissionValidator.validateForUpdate(userEntity));
    }

    @Test
    void validateForDelete_Self_NoException() {
        setupAuthentication("PET_OWNER", "user@example.com", true);
        assertDoesNotThrow(() -> userPermissionValidator.validateForDelete(userEntity));
    }

    @Test
    void validateForDelete_AdminOnAny_NoException() {
        setupAuthentication("ADMIN", "admin@example.com", true);
        assertDoesNotThrow(() -> userPermissionValidator.validateForDelete(userEntity));
    }

    @Test
    void validateForGetByRole_AsAdmin_NoException() {
        setupAuthentication("ADMIN", "admin@example.com", true);
        assertDoesNotThrow(() -> userPermissionValidator.validateForGetByRole(UserRole.PET_OWNER));
    }
}
