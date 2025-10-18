package ua.edu.ukma.objectanalysis.openvet.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.AppointmentEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.examination.MedicalRecordsEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PetEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.VeterinarianEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.examination.MedicalRecordsRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.NotFoundException;
import ua.edu.ukma.objectanalysis.openvet.merger.BaseMerger;
import ua.edu.ukma.objectanalysis.openvet.repository.examination.MedicalRecordsRepository;
import ua.edu.ukma.objectanalysis.openvet.validator.data.BaseValidator;
import ua.edu.ukma.objectanalysis.openvet.validator.permission.BasePermissionValidator;
import ua.edu.ukma.objectanalysis.openvet.validator.permission.MedicalRecordsPermissionValidator;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MedicalRecordsService Tests")
class MedicalRecordsServiceTest {

    @Mock
    private MedicalRecordsRepository medicalRecordsRepository;

    @Mock
    private MedicalRecordsPermissionValidator medicalPermissionValidator;

    @Mock
    private BaseMerger<MedicalRecordsEntity, MedicalRecordsRequest> merger;

    @Mock
    private BaseValidator<MedicalRecordsEntity, MedicalRecordsRequest> validator;

    @Mock
    private BasePermissionValidator<MedicalRecordsEntity, MedicalRecordsRequest> permissionValidator;

    @InjectMocks
    private MedicalRecordsService medicalRecordsService;

    private MedicalRecordsEntity testRecord;
    private MedicalRecordsRequest testRequest;
    private AppointmentEntity testAppointment;
    private PetEntity testPet;
    private VeterinarianEntity testVet;

    @BeforeEach
    void setUp() {
        testVet = new VeterinarianEntity();
        testVet.setId(1L);
        testVet.setEmail("vet@test.com");

        testPet = PetEntity.builder()
                .id(1L)
                .name("Fluffy")
                .build();

        testAppointment = AppointmentEntity.builder()
                .id(1L)
                .pet(testPet)
                .build();

        testRecord = MedicalRecordsEntity.builder()
                .id(1L)
                .appointment(testAppointment)
                .diagnosis("Healthy")
                .treatment("Regular checkup")
                .notes("All vitals normal")
                .build();

        testRequest = MedicalRecordsRequest.builder()
                .appointmentId(1L)
                .diagnosis("Healthy")
                .treatment("Regular checkup")
                .notes("All vitals normal")
                .build();

        // Set up the dependencies inherited from BaseService
        ReflectionTestUtils.setField(medicalRecordsService, "repository", medicalRecordsRepository);
        ReflectionTestUtils.setField(medicalRecordsService, "merger", merger);
        ReflectionTestUtils.setField(medicalRecordsService, "validator", validator);
        ReflectionTestUtils.setField(medicalRecordsService, "permissionValidator", permissionValidator);
    }

    // ===== CREATE Tests =====

    @Test
    @DisplayName("Should create medical record successfully")
    void testCreate_Success() {
        // Arrange
        when(medicalRecordsRepository.saveAndFlush(any(MedicalRecordsEntity.class)))
                .thenReturn(testRecord);
        doNothing().when(permissionValidator).validateForCreate(any());
        doNothing().when(validator).validateForCreate(any());
        doNothing().when(merger).mergeCreate(any(), any());

        // Act
        MedicalRecordsEntity result = medicalRecordsService.create(testRequest);

        // Assert
        assertNotNull(result);
        assertEquals(testRecord.getId(), result.getId());
        verify(permissionValidator).validateForCreate(testRequest);
        verify(validator).validateForCreate(testRequest);
        verify(medicalRecordsRepository).saveAndFlush(any(MedicalRecordsEntity.class));
    }

    // ===== GET BY VETERINARIAN Tests =====

    @Test
    @DisplayName("Should get medical records by veterinarian successfully")
    void testGetByVeterinarian_Success() {
        // Arrange
        List<MedicalRecordsEntity> records = Arrays.asList(testRecord);
        when(medicalRecordsRepository.findAllByAppointment_TimeSlot_Veterinarian_Id(1L))
                .thenReturn(records);
        doNothing().when(medicalPermissionValidator).validateForGetByVeterinarian(1L);

        // Act
        List<MedicalRecordsEntity> result = medicalRecordsService.getByVeterinarian(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(medicalPermissionValidator).validateForGetByVeterinarian(1L);
        verify(medicalRecordsRepository).findAllByAppointment_TimeSlot_Veterinarian_Id(1L);
    }

    @Test
    @DisplayName("Should return empty list when no records for veterinarian")
    void testGetByVeterinarian_EmptyList() {
        // Arrange
        when(medicalRecordsRepository.findAllByAppointment_TimeSlot_Veterinarian_Id(999L))
                .thenReturn(Arrays.asList());
        doNothing().when(medicalPermissionValidator).validateForGetByVeterinarian(999L);

        // Act
        List<MedicalRecordsEntity> result = medicalRecordsService.getByVeterinarian(999L);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    // ===== GET BY PET Tests =====

    @Test
    @DisplayName("Should get medical records by pet successfully")
    void testGetByPet_Success() {
        // Arrange
        List<MedicalRecordsEntity> records = Arrays.asList(testRecord);
        when(medicalRecordsRepository.findAllByAppointment_Pet_Id(1L))
                .thenReturn(records);
        doNothing().when(medicalPermissionValidator).validateForGetByPet(1L);

        // Act
        List<MedicalRecordsEntity> result = medicalRecordsService.getByPet(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(medicalPermissionValidator).validateForGetByPet(1L);
        verify(medicalRecordsRepository).findAllByAppointment_Pet_Id(1L);
    }

    @Test
    @DisplayName("Should return empty list when no records for pet")
    void testGetByPet_EmptyList() {
        // Arrange
        when(medicalRecordsRepository.findAllByAppointment_Pet_Id(999L))
                .thenReturn(Arrays.asList());
        doNothing().when(medicalPermissionValidator).validateForGetByPet(999L);

        // Act
        List<MedicalRecordsEntity> result = medicalRecordsService.getByPet(999L);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    // ===== GET BY PET OWNER Tests =====

    @Test
    @DisplayName("Should get medical records by pet owner successfully")
    void testGetByPetOwner_Success() {
        // Arrange
        List<MedicalRecordsEntity> records = Arrays.asList(testRecord);
        when(medicalRecordsRepository.findAllByAppointment_Pet_Owner_Id(1L))
                .thenReturn(records);
        doNothing().when(medicalPermissionValidator).validateForGetByPetOwner(1L);

        // Act
        List<MedicalRecordsEntity> result = medicalRecordsService.getByPetOwner(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(medicalPermissionValidator).validateForGetByPetOwner(1L);
        verify(medicalRecordsRepository).findAllByAppointment_Pet_Owner_Id(1L);
    }

    @Test
    @DisplayName("Should return empty list when no records for owner")
    void testGetByPetOwner_EmptyList() {
        // Arrange
        when(medicalRecordsRepository.findAllByAppointment_Pet_Owner_Id(999L))
                .thenReturn(Arrays.asList());
        doNothing().when(medicalPermissionValidator).validateForGetByPetOwner(999L);

        // Act
        List<MedicalRecordsEntity> result = medicalRecordsService.getByPetOwner(999L);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    // ===== GET BY ID Tests =====

    @Test
    @DisplayName("Should get medical record by id successfully")
    void testGetById_Success() {
        // Arrange
        when(medicalRecordsRepository.findById(1L)).thenReturn(Optional.of(testRecord));
        doNothing().when(permissionValidator).validateForGet(any());

        // Act
        MedicalRecordsEntity result = medicalRecordsService.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(medicalRecordsRepository).findById(1L);
        verify(permissionValidator).validateForGet(testRecord);
    }

    @Test
    @DisplayName("Should throw NotFoundException when record not found")
    void testGetById_NotFound() {
        // Arrange
        when(medicalRecordsRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> medicalRecordsService.getById(999L));
        verify(medicalRecordsRepository).findById(999L);
    }

    // ===== UPDATE Tests =====

    @Test
    @DisplayName("Should update medical record successfully")
    void testUpdate_Success() {
        // Arrange
        when(medicalRecordsRepository.findById(1L)).thenReturn(Optional.of(testRecord));
        when(medicalRecordsRepository.saveAndFlush(any(MedicalRecordsEntity.class)))
                .thenReturn(testRecord);
        doNothing().when(permissionValidator).validateForGet(any());
        doNothing().when(permissionValidator).validateForUpdate(any());
        doNothing().when(validator).validateForUpdate(any(), any());
        doNothing().when(merger).mergeUpdate(any(), any());

        // Act
        MedicalRecordsEntity result = medicalRecordsService.update(1L, testRequest);

        // Assert
        assertNotNull(result);
        verify(medicalRecordsRepository).findById(1L);
        verify(permissionValidator).validateForUpdate(testRecord);
        verify(validator).validateForUpdate(testRequest, testRecord);
        verify(medicalRecordsRepository).saveAndFlush(testRecord);
    }

    // ===== DELETE Tests =====

    @Test
    @DisplayName("Should delete medical record successfully")
    void testDeleteById_Success() {
        // Arrange
        when(medicalRecordsRepository.findById(1L)).thenReturn(Optional.of(testRecord));
        doNothing().when(permissionValidator).validateForGet(any());
        doNothing().when(permissionValidator).validateForDelete(any());
        doNothing().when(validator).validateForDelete(any());
        doNothing().when(medicalRecordsRepository).deleteById(1L);

        // Act
        MedicalRecordsEntity result = medicalRecordsService.deleteById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(medicalRecordsRepository).deleteById(1L);
    }

    // ===== GET ALL Tests =====

    @Test
    @DisplayName("Should get all medical records successfully")
    void testGetAll_Success() {
        // Arrange
        List<MedicalRecordsEntity> records = Arrays.asList(testRecord);
        when(medicalRecordsRepository.findAll()).thenReturn(records);
        doNothing().when(permissionValidator).validateForGetAll();

        // Act
        List<MedicalRecordsEntity> result = medicalRecordsService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(medicalRecordsRepository).findAll();
        verify(permissionValidator).validateForGetAll();
    }
}

