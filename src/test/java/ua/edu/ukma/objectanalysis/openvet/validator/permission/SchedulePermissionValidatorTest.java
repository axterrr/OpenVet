package ua.edu.ukma.objectanalysis.openvet.validator.permission;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.ScheduleEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.UserEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.VeterinarianEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;
import ua.edu.ukma.objectanalysis.openvet.dto.appointment.ScheduleRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.ForbiddenException;
import ua.edu.ukma.objectanalysis.openvet.repository.user.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchedulePermissionValidatorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SchedulePermissionValidator schedulePermissionValidator;

    private ScheduleEntity scheduleEntity;
    private ScheduleRequest scheduleRequest;
    private VeterinarianEntity veterinarianEntity;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        veterinarianEntity = new VeterinarianEntity();
        veterinarianEntity.setId(1L);
        veterinarianEntity.setEmail("vet@example.com");

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("vet@example.com");

        scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setVeterinarian(veterinarianEntity);

        scheduleRequest = new ScheduleRequest();
        scheduleRequest.setVeterinarianId(1L);

        lenient().when(userRepository.findByEmail("vet@example.com")).thenReturn(Optional.of(userEntity));

        schedulePermissionValidator = new SchedulePermissionValidator(userRepository) {
            @Override
            protected boolean isAuthenticatedUserAdmin() {
                return "ADMIN".equals(role);
            }

            @Override
            protected boolean isAuthenticatedUserVeterinarian() {
                return "VET".equals(role);
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
        assertDoesNotThrow(() -> schedulePermissionValidator.validateForGetAll());
    }

    @Test
    void validateForGetAll_AsVet_ThrowsForbiddenException() {
        setupAuthentication("VET", "vet@example.com");
        assertThrows(ForbiddenException.class, () -> schedulePermissionValidator.validateForGetAll());
    }

    @Test
    void validateForGet_AnyRole_NoException() {
        setupAuthentication("ANY", "any@example.com");
        assertDoesNotThrow(() -> schedulePermissionValidator.validateForGet(scheduleEntity));
    }

    @Test
    void validateForCreate_AsAssignedVet_NoException() {
        setupAuthentication("VET", "vet@example.com");
        assertDoesNotThrow(() -> schedulePermissionValidator.validateForCreate(scheduleRequest));
    }

    @Test
    void validateForCreate_AsDifferentVet_ThrowsForbiddenException() {
        setupAuthentication("VET", "othervet@example.com");
        assertThrows(ForbiddenException.class, () -> schedulePermissionValidator.validateForCreate(scheduleRequest));
    }

    @Test
    void validateForUpdate_AsAssignedVet_NoException() {
        setupAuthentication("VET", "vet@example.com");
        assertDoesNotThrow(() -> schedulePermissionValidator.validateForUpdate(scheduleEntity));
    }

    @Test
    void validateForUpdate_AsDifferentVet_ThrowsForbiddenException() {
        setupAuthentication("VET", "othervet@example.com");
        assertThrows(ForbiddenException.class, () -> schedulePermissionValidator.validateForUpdate(scheduleEntity));
    }

    @Test
    void validateForDelete_AsAdmin_NoException() {
        setupAuthentication("ADMIN", "admin@example.com");
        assertDoesNotThrow(() -> schedulePermissionValidator.validateForDelete(scheduleEntity));
    }
}
