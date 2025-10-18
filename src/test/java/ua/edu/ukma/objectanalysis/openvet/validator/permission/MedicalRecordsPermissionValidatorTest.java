package ua.edu.ukma.objectanalysis.openvet.validator.permission;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.AppointmentEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.TimeSlotEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.examination.MedicalRecordsEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PetEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.PetOwnerEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.VeterinarianEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;
import ua.edu.ukma.objectanalysis.openvet.dto.examination.MedicalRecordsRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.ForbiddenException;
import ua.edu.ukma.objectanalysis.openvet.repository.appointment.AppointmentRepository;
import ua.edu.ukma.objectanalysis.openvet.repository.pet.PetRepository;
import ua.edu.ukma.objectanalysis.openvet.repository.user.PetOwnerRepository;
import ua.edu.ukma.objectanalysis.openvet.repository.user.VeterinarianRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedicalRecordsPermissionValidatorTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private VeterinarianRepository veterinarianRepository;
    @Mock
    private PetOwnerRepository petOwnerRepository;
    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private MedicalRecordsPermissionValidator medicalRecordsPermissionValidator;

    private MedicalRecordsEntity medicalRecordsEntity;
    private MedicalRecordsRequest medicalRecordsRequest;
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

        medicalRecordsEntity = new MedicalRecordsEntity();
        medicalRecordsEntity.setId(1L);
        medicalRecordsEntity.setAppointment(appointmentEntity);

        medicalRecordsRequest = new MedicalRecordsRequest();
        medicalRecordsRequest.setAppointmentId(1L);

        lenient().when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointmentEntity));
        lenient().when(veterinarianRepository.findById(1L)).thenReturn(Optional.of(veterinarianEntity));
        lenient().when(petOwnerRepository.findById(1L)).thenReturn(Optional.of(petOwnerEntity));
        lenient().when(petRepository.findById(1L)).thenReturn(Optional.of(petEntity));

        medicalRecordsPermissionValidator = new MedicalRecordsPermissionValidator(appointmentRepository, veterinarianRepository, petOwnerRepository, petRepository) {
            @Override
            protected boolean isAuthenticatedUserAdmin() {
                return "ADMIN".equals(role);
            }

            @Override
            protected boolean isAuthenticatedUserVeterinarian() {
                return "VET".equals(role);
            }

            @Override
            protected boolean isAuthenticatedUserPetOwner() {
                return "OWNER".equals(role);
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
        assertDoesNotThrow(() -> medicalRecordsPermissionValidator.validateForGetAll());
    }

    @Test
    void validateForGet_AsAssignedVet_NoException() {
        setupAuthentication("VET", "vet@example.com");
        assertDoesNotThrow(() -> medicalRecordsPermissionValidator.validateForGet(medicalRecordsEntity));
    }

    @Test
    void validateForGet_AsPetOwner_NoException() {
        setupAuthentication("OWNER", "owner@example.com");
        assertDoesNotThrow(() -> medicalRecordsPermissionValidator.validateForGet(medicalRecordsEntity));
    }

    @Test
    void validateForCreate_AsAssignedVet_NoException() {
        setupAuthentication("VET", "vet@example.com");
        assertDoesNotThrow(() -> medicalRecordsPermissionValidator.validateForCreate(medicalRecordsRequest));
    }

    @Test
    void validateForUpdate_AsAssignedVet_NoException() {
        setupAuthentication("VET", "vet@example.com");
        assertDoesNotThrow(() -> medicalRecordsPermissionValidator.validateForUpdate(medicalRecordsEntity));
    }

    @Test
    void validateForDelete_AsAdmin_NoException() {
        setupAuthentication("ADMIN", "admin@example.com");
        assertDoesNotThrow(() -> medicalRecordsPermissionValidator.validateForDelete(medicalRecordsEntity));
    }

    @Test
    void validateForGetByVeterinarian_AsSameVet_NoException() {
        setupAuthentication("VET", "vet@example.com");
        assertDoesNotThrow(() -> medicalRecordsPermissionValidator.validateForGetByVeterinarian(1L));
    }

    @Test
    void validateForGetByPet_AsOwner_NoException() {
        setupAuthentication("OWNER", "owner@example.com");
        assertDoesNotThrow(() -> medicalRecordsPermissionValidator.validateForGetByPet(1L));
    }

    @Test
    void validateForGetByPetOwner_AsSameOwner_NoException() {
        setupAuthentication("OWNER", "owner@example.com");
        assertDoesNotThrow(() -> medicalRecordsPermissionValidator.validateForGetByPetOwner(1L));
    }
}
