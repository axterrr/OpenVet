package ua.edu.ukma.objectanalysis.openvet.validator.permission;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PendingOwnerEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PetEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.PetOwnerEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.UserEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;
import ua.edu.ukma.objectanalysis.openvet.dto.pet.PetRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.ForbiddenException;
import ua.edu.ukma.objectanalysis.openvet.repository.user.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetPermissionValidatorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PetPermissionValidator petPermissionValidator;

    private PetEntity petEntity;
    private PetRequest petRequest;
    private PetOwnerEntity petOwnerEntity;
    private UserEntity userEntity;
    private PendingOwnerEntity pendingOwnerEntity;

    @BeforeEach
    void setUp() {
        petOwnerEntity = new PetOwnerEntity();
        petOwnerEntity.setId(1L);
        petOwnerEntity.setEmail("owner@example.com");
        petOwnerEntity.setPhoneNumber("1234567890");

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("owner@example.com");
        userEntity.setPhoneNumber("1234567890");

        pendingOwnerEntity = new PendingOwnerEntity();
        pendingOwnerEntity.setPhoneNumber("1234567890");

        petEntity = new PetEntity();
        petEntity.setId(1L);
        petEntity.setOwner(petOwnerEntity);

        petRequest = new PetRequest();
        petRequest.setOwnerPhone("1234567890");

        lenient().when(userRepository.findByEmail("owner@example.com")).thenReturn(Optional.of(userEntity));

        petPermissionValidator = new PetPermissionValidator(userRepository) {
            @Override
            protected boolean isAuthenticatedUserAdmin() {
                return "ADMIN".equals(role);
            }

            @Override
            protected boolean isAuthenticatedUserVeterinarian() {
                return "VETERINARIAN".equals(role);
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

    private void setupAuthentication(String role, String email) {
        this.role = role;
        this.email = email;
    }

    @Test
    void validateForGetAll_AsAdmin_NoException() {
        setupAuthentication("ADMIN", "admin@example.com");
        assertDoesNotThrow(() -> petPermissionValidator.validateForGetAll());
    }

    @Test
    void validateForGet_AsOwner_NoException() {
        setupAuthentication("PET_OWNER", "owner@example.com");
        assertDoesNotThrow(() -> petPermissionValidator.validateForGet(petEntity));
    }

    @Test
    void validateForGet_AsPendingOwner_NoException() {
        petEntity.setOwner(null);
        petEntity.setPendingOwner(pendingOwnerEntity);
        setupAuthentication("PET_OWNER", "owner@example.com");
        assertDoesNotThrow(() -> petPermissionValidator.validateForGet(petEntity));
    }

    @Test
    void validateForCreate_AsOwner_NoException() {
        setupAuthentication("PET_OWNER", "owner@example.com");
        assertDoesNotThrow(() -> petPermissionValidator.validateForCreate(petRequest));
    }

    @Test
    void validateForUpdate_AsOwner_NoException() {
        setupAuthentication("PET_OWNER", "owner@example.com");
        assertDoesNotThrow(() -> petPermissionValidator.validateForUpdate(petEntity));
    }

    @Test
    void validateForDelete_AsAdmin_NoException() {
        setupAuthentication("ADMIN", "admin@example.com");
        assertDoesNotThrow(() -> petPermissionValidator.validateForDelete(petEntity));
    }

    @Test
    void validateForOwnershipChange_AsVet_NoException() {
        setupAuthentication("VETERINARIAN", "vet@example.com");
        assertDoesNotThrow(() -> petPermissionValidator.validateForOwnershipChange(petEntity));
    }

    @Test
    void validateForSetPendingOwner_AsOwner_NoException() {
        setupAuthentication("PET_OWNER", "owner@example.com");
        assertDoesNotThrow(() -> petPermissionValidator.validateForSetPendingOwner(petEntity));
    }

    @Test
    void validateForAddVaccinationRecord_AsVet_NoException() {
        setupAuthentication("VETERINARIAN", "vet@example.com");
        assertDoesNotThrow(() -> petPermissionValidator.validateForAddVaccinationRecord(petEntity));
    }

    @Test
    void validateForAddVaccinationRecord_AsAdmin_ThrowsForbiddenException() {
        setupAuthentication("ADMIN", "admin@example.com");
        assertThrows(ForbiddenException.class, () -> petPermissionValidator.validateForAddVaccinationRecord(petEntity));
    }
}
