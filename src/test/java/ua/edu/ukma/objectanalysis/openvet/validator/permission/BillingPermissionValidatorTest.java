package ua.edu.ukma.objectanalysis.openvet.validator.permission;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.AppointmentEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.TimeSlotEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.billing.BillingEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PetEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.PetOwnerEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.VeterinarianEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;
import ua.edu.ukma.objectanalysis.openvet.dto.billing.BillingRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.ForbiddenException;
import ua.edu.ukma.objectanalysis.openvet.repository.appointment.AppointmentRepository;
import ua.edu.ukma.objectanalysis.openvet.repository.user.PetOwnerRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class BillingPermissionValidatorTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PetOwnerRepository petOwnerRepository;

    @InjectMocks
    private BillingPermissionValidator billingPermissionValidator;

    private BillingEntity billingEntity;
    private BillingRequest billingRequest;
    private AppointmentEntity appointmentEntity;
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

        billingEntity = new BillingEntity();
        billingEntity.setId(1L);
        billingEntity.setAppointment(appointmentEntity);

        billingRequest = new BillingRequest();
        billingRequest.setAppointmentId(1L);

        lenient().when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointmentEntity));
        lenient().when(petOwnerRepository.findById(1L)).thenReturn(Optional.of(petOwnerEntity));

        billingPermissionValidator = new BillingPermissionValidator(appointmentRepository, petOwnerRepository) {
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
        assertDoesNotThrow(() -> billingPermissionValidator.validateForGetAll());
    }

    @Test
    void validateForGetAll_AsVet_ThrowsForbiddenException() {
        setupAuthentication("VETERINARIAN", "vet@example.com");
        assertThrows(ForbiddenException.class, () -> billingPermissionValidator.validateForGetAll());
    }

    @Test
    void validateForGet_AsOwner_NoException() {
        setupAuthentication("PET_OWNER", "owner@example.com");
        assertDoesNotThrow(() -> billingPermissionValidator.validateForGet(billingEntity));
    }

    @Test
    void validateForCreate_AsAssignedVet_NoException() {
        setupAuthentication("VETERINARIAN", "vet@example.com");
        assertDoesNotThrow(() -> billingPermissionValidator.validateForCreate(billingRequest));
    }

    @Test
    void validateForCreate_AsDifferentVet_ThrowsForbiddenException() {
        setupAuthentication("VETERINARIAN", "othervet@example.com");
        assertThrows(ForbiddenException.class, () -> billingPermissionValidator.validateForCreate(billingRequest));
    }

    @Test
    void validateForUpdate_AsAdmin_NoException() {
        setupAuthentication("ADMIN", "admin@example.com");
        assertDoesNotThrow(() -> billingPermissionValidator.validateForUpdate(billingEntity));
    }

    @Test
    void validateForDelete_AsAdmin_NoException() {
        setupAuthentication("ADMIN", "admin@example.com");
        assertDoesNotThrow(() -> billingPermissionValidator.validateForDelete(billingEntity));
    }

    @Test
    void validateForGetByPetOwner_AsOwner_NoException() {
        setupAuthentication("PET_OWNER", "owner@example.com");
        assertDoesNotThrow(() -> billingPermissionValidator.validateForGetByPetOwner(1L));
    }

    @Test
    void validateForGetByPetOwner_AsDifferentOwner_ThrowsForbiddenException() {
        setupAuthentication("PET_OWNER", "otherowner@example.com");
        assertThrows(ForbiddenException.class, () -> billingPermissionValidator.validateForGetByPetOwner(1L));
    }
}
