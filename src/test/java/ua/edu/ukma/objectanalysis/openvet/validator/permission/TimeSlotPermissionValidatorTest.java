package ua.edu.ukma.objectanalysis.openvet.validator.permission;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.AppointmentEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.TimeSlotEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PetEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.PetOwnerEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.UserEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.VeterinarianEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;
import ua.edu.ukma.objectanalysis.openvet.dto.appointment.TimeSlotRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.ForbiddenException;
import ua.edu.ukma.objectanalysis.openvet.repository.user.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeSlotPermissionValidatorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TimeSlotPermissionValidator timeSlotPermissionValidator;

    private TimeSlotEntity timeSlotEntity;
    private TimeSlotRequest timeSlotRequest;
    private VeterinarianEntity veterinarianEntity;
    private PetOwnerEntity petOwnerEntity;
    private PetEntity petEntity;
    private AppointmentEntity appointmentEntity;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        veterinarianEntity = new VeterinarianEntity();
        veterinarianEntity.setId(1L);
        veterinarianEntity.setEmail("vet@example.com");

        petOwnerEntity = new PetOwnerEntity();
        petOwnerEntity.setId(1L);
        petOwnerEntity.setEmail("owner@example.com");

        petEntity = new PetEntity();
        petEntity.setId(1L);
        petEntity.setOwner(petOwnerEntity);

        appointmentEntity = new AppointmentEntity();
        appointmentEntity.setId(1L);
        appointmentEntity.setPet(petEntity);

        timeSlotEntity = new TimeSlotEntity();
        timeSlotEntity.setId(1L);
        timeSlotEntity.setVeterinarian(veterinarianEntity);
        timeSlotEntity.setAppointment(appointmentEntity);

        timeSlotRequest = new TimeSlotRequest();
        timeSlotRequest.setVeterinarianId(1L);

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("vet@example.com");

        lenient().when(userRepository.findByEmail("vet@example.com")).thenReturn(Optional.of(userEntity));

        timeSlotPermissionValidator = new TimeSlotPermissionValidator(userRepository) {
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

    private void setupAuthentication(String role, String email) {
        this.role = role;
        this.email = email;
    }

    @Test
    void validateForGetAll_AsVet_NoException() {
        setupAuthentication("VETERINARIAN", "vet@example.com");
        assertDoesNotThrow(() -> timeSlotPermissionValidator.validateForGetAll());
    }

    @Test
    void validateForGet_AsAssignedVet_NoException() {
        setupAuthentication("VETERINARIAN", "vet@example.com");
        assertDoesNotThrow(() -> timeSlotPermissionValidator.validateForGet(timeSlotEntity));
    }

    @Test
    void validateForGet_AsOwnerWithAppointment_NoException() {
        setupAuthentication("PET_OWNER", "owner@example.com");
        assertDoesNotThrow(() -> timeSlotPermissionValidator.validateForGet(timeSlotEntity));
    }

    @Test
    void validateForCreate_AsAdmin_NoException() {
        setupAuthentication("ADMIN", "admin@example.com");
        assertDoesNotThrow(() -> timeSlotPermissionValidator.validateForCreate(timeSlotRequest));
    }

    @Test
    void validateForUpdate_AsAssignedVet_NoException() {
        setupAuthentication("VETERINARIAN", "vet@example.com");
        assertDoesNotThrow(() -> timeSlotPermissionValidator.validateForUpdate(timeSlotEntity));
    }

    @Test
    void validateForDelete_AsAdmin_NoException() {
        setupAuthentication("ADMIN", "admin@example.com");
        assertDoesNotThrow(() -> timeSlotPermissionValidator.validateForDelete(timeSlotEntity));
    }

    @Test
    void validateForGenerate_AsAdmin_NoException() {
        setupAuthentication("ADMIN", "admin@example.com");
        assertDoesNotThrow(() -> timeSlotPermissionValidator.validateForGenerate());
    }

    @Test
    void validateForGenerate_AsVet_ThrowsForbiddenException() {
        setupAuthentication("VETERINARIAN", "vet@example.com");
        assertThrows(ForbiddenException.class, () -> timeSlotPermissionValidator.validateForGenerate());
    }
}
