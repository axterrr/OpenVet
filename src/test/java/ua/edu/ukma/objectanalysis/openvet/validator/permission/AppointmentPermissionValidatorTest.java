package ua.edu.ukma.objectanalysis.openvet.validator.permission;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.AppointmentEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.TimeSlotEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PetEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.PetOwnerEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.VeterinarianEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.appointment.AppointmentRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.ForbiddenException;
import ua.edu.ukma.objectanalysis.openvet.repository.pet.PetRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentPermissionValidatorTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private AppointmentPermissionValidator appointmentPermissionValidator;

    private AppointmentEntity appointmentEntity;
    private AppointmentRequest appointmentRequest;
    private PetEntity petEntity;
    private PetOwnerEntity petOwnerEntity;
    private VeterinarianEntity veterinarianEntity;
    private TimeSlotEntity timeSlotEntity;

    @BeforeEach
    void setUp() {
        petOwnerEntity = new PetOwnerEntity();
        petOwnerEntity.setId(1L);
        petOwnerEntity.setEmail("owner@example.com");

        petEntity = new PetEntity();
        petEntity.setId(1L);
        petEntity.setOwner(petOwnerEntity);

        veterinarianEntity = new VeterinarianEntity();
        veterinarianEntity.setId(1L);
        veterinarianEntity.setEmail("vet@example.com");

        timeSlotEntity = new TimeSlotEntity();
        timeSlotEntity.setId(1L);
        timeSlotEntity.setVeterinarian(veterinarianEntity);

        appointmentEntity = new AppointmentEntity();
        appointmentEntity.setId(1L);
        appointmentEntity.setPet(petEntity);
        appointmentEntity.setTimeSlot(timeSlotEntity);

        appointmentRequest = new AppointmentRequest();
        appointmentRequest.setPetId(1L);

        lenient().when(petRepository.findById(1L)).thenReturn(Optional.of(petEntity));
    }

    private void setupAuthentication(String role, String email) {
        appointmentPermissionValidator = new AppointmentPermissionValidator(petRepository) {
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
        };
    }

    @Test
    void validateForGetAll_AsAdmin_NoException() {
        setupAuthentication("ADMIN", "admin@example.com");
        assertDoesNotThrow(() -> appointmentPermissionValidator.validateForGetAll());
    }

    @Test
    void validateForGetAll_AsVet_NoException() {
        setupAuthentication("VETERINARIAN", "vet@example.com");
        assertDoesNotThrow(() -> appointmentPermissionValidator.validateForGetAll());
    }

    @Test
    void validateForGetAll_AsOwner_ThrowsAccessDenied() {
        setupAuthentication("PET_OWNER", "owner@example.com");
        assertThrows(ForbiddenException.class, () -> appointmentPermissionValidator.validateForGetAll());
    }

    @Test
    void validateForGet_AsOwnerOfPet_NoException() {
        setupAuthentication("PET_OWNER", "owner@example.com");
        assertDoesNotThrow(() -> appointmentPermissionValidator.validateForGet(appointmentEntity));
    }

    @Test
    void validateForGet_AsDifferentOwner_ThrowsAccessDenied() {
        setupAuthentication("PET_OWNER", "otherowner@example.com");
        assertThrows(ForbiddenException.class, () -> appointmentPermissionValidator.validateForGet(appointmentEntity));
    }

    @Test
    void validateForCreate_AsOwnerOfPet_NoException() {
        setupAuthentication("PET_OWNER", "owner@example.com");
        assertDoesNotThrow(() -> appointmentPermissionValidator.validateForCreate(appointmentRequest));
    }

    @Test
    void validateForUpdate_AsAssignedVet_NoException() {
        setupAuthentication("VETERINARIAN", "vet@example.com");
        assertDoesNotThrow(() -> appointmentPermissionValidator.validateForUpdate(appointmentEntity));
    }

    @Test
    void validateForUpdate_AsDifferentVet_ThrowsAccessDenied() {
        setupAuthentication("VETERINARIAN", "othervet@example.com");
        assertThrows(ForbiddenException.class, () -> appointmentPermissionValidator.validateForUpdate(appointmentEntity));
    }

    @Test
    void validateForClinicCancel_AsAdmin_NoException() {
        setupAuthentication("ADMIN", "admin@example.com");
        assertDoesNotThrow(() -> appointmentPermissionValidator.validateForClinicCancel(appointmentEntity));
    }

    @Test
    void validateForClinicCancel_AsOwner_ThrowsAccessDenied() {
        setupAuthentication("PET_OWNER", "owner@example.com");
        assertThrows(ForbiddenException.class, () -> appointmentPermissionValidator.validateForClinicCancel(appointmentEntity));
    }

    @Test
    void validateForOwnerCancel_AsOwner_NoException() {
        setupAuthentication("PET_OWNER", "owner@example.com");
        assertDoesNotThrow(() -> appointmentPermissionValidator.validateForOwnerCancel(appointmentEntity));
    }

    @Test
    void validateForOwnerCancel_AsVet_ThrowsAccessDenied() {
        setupAuthentication("VETERINARIAN", "vet@example.com");
        assertThrows(ForbiddenException.class, () -> appointmentPermissionValidator.validateForOwnerCancel(appointmentEntity));
    }
}
