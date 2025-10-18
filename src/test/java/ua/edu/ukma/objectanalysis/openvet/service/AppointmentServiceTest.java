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
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.TimeSlotEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PetEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.AppointmentStatus;
import ua.edu.ukma.objectanalysis.openvet.dto.appointment.AppointmentRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.NotFoundException;
import ua.edu.ukma.objectanalysis.openvet.exception.ValidationException;
import ua.edu.ukma.objectanalysis.openvet.merger.AppointmentMerger;
import ua.edu.ukma.objectanalysis.openvet.repository.appointment.AppointmentRepository;
import ua.edu.ukma.objectanalysis.openvet.validator.data.AppointmentValidator;
import ua.edu.ukma.objectanalysis.openvet.validator.permission.AppointmentPermissionValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AppointmentService Tests")
class AppointmentServiceTest {

    @InjectMocks
    private AppointmentService appointmentService;
    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private AppointmentPermissionValidator permissionValidator;
    @Mock
    private AppointmentValidator validator;
    @Mock
    private AppointmentMerger merger;
    private AppointmentEntity testAppointment;
    private AppointmentRequest testRequest;

    @BeforeEach
    void setUp() {

        PetEntity testPet = PetEntity.builder()
                .id(1L)
                .name("Fluffy")
                .build();

        TimeSlotEntity testTimeSlot = TimeSlotEntity.builder()
                .id(1L)
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(1).plusHours(1))
                .build();

        testAppointment = AppointmentEntity.builder()
                .id(1L)
                .pet(testPet)
                .timeSlot(testTimeSlot)
                .status(AppointmentStatus.SCHEDULED)
                .build();

        testRequest = AppointmentRequest.builder()
                .petId(1L)
                .timeSlotId(1L)
                .reasonForVisit("Regular checkup")
                .status(AppointmentStatus.SCHEDULED)
                .build();
    }

    // ===== CREATE Tests =====

    @Test
    @DisplayName("Should create appointment successfully")
    void testCreate_Success() {
        // Arrange
        when(appointmentRepository.saveAndFlush(any(AppointmentEntity.class))).thenReturn(testAppointment);
        doNothing().when(permissionValidator).validateForCreate(any());
        doNothing().when(validator).validateForCreate(any());
        doNothing().when(merger).mergeCreate(any(), any());

        // Act
        AppointmentEntity result = appointmentService.create(testRequest);

        // Assert
        assertNotNull(result);
        assertEquals(testAppointment.getId(), result.getId());
        verify(permissionValidator).validateForCreate(testRequest);
        verify(validator).validateForCreate(testRequest);
        verify(merger).mergeCreate(any(AppointmentEntity.class), eq(testRequest));
        verify(appointmentRepository).saveAndFlush(any(AppointmentEntity.class));
    }

    @Test
    @DisplayName("Should throw exception when creating appointment with invalid data")
    void testCreate_ValidationException() {
        // Arrange
        doThrow(new ValidationException("Invalid appointment data")).when(validator).validateForCreate(any());

        // Act & Assert
        assertThrows(ValidationException.class, () -> appointmentService.create(testRequest));
        verify(validator).validateForCreate(testRequest);
        verify(appointmentRepository, never()).saveAndFlush(any());
    }

    // ===== UPDATE Tests =====

    @Test
    @DisplayName("Should update appointment successfully")
    void testUpdate_Success() {
        // Arrange
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));
        when(appointmentRepository.saveAndFlush(any(AppointmentEntity.class))).thenReturn(testAppointment);
        doNothing().when(permissionValidator).validateForGet(any());
        doNothing().when(permissionValidator).validateForUpdate(any());
        doNothing().when(validator).validateForUpdate(any(), any());
        doNothing().when(merger).mergeUpdate(any(), any());

        // Act
        AppointmentEntity result = appointmentService.update(1L, testRequest);

        // Assert
        assertNotNull(result);
        verify(appointmentRepository).findById(1L);
        verify(permissionValidator).validateForUpdate(testAppointment);
        verify(validator).validateForUpdate(testRequest, testAppointment);
        verify(appointmentRepository).saveAndFlush(testAppointment);
    }

    @Test
    @DisplayName("Should throw NotFoundException when updating non-existent appointment")
    void testUpdate_NotFound() {
        // Arrange
        when(appointmentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> appointmentService.update(999L, testRequest));
        verify(appointmentRepository).findById(999L);
        verify(appointmentRepository, never()).saveAndFlush(any());
    }

    @Test
    @DisplayName("Should validate clinic cancel permission when status is CANCELED_BY_CLINIC")
    void testUpdate_ClinicCancel() {
        // Arrange
        testRequest.setStatus(AppointmentStatus.CANCELED_BY_CLINIC);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));
        when(appointmentRepository.saveAndFlush(any(AppointmentEntity.class))).thenReturn(testAppointment);
        doNothing().when(permissionValidator).validateForGet(any());
        doNothing().when(permissionValidator).validateForUpdate(any());
        doNothing().when(permissionValidator).validateForClinicCancel(any());
        doNothing().when(validator).validateForUpdate(any(), any());
        doNothing().when(merger).mergeUpdate(any(), any());

        // Act
        appointmentService.update(1L, testRequest);

        // Assert
        verify(permissionValidator).validateForClinicCancel(testAppointment);
    }

    @Test
    @DisplayName("Should validate owner cancel permission when status is CANCELED_BY_OWNER")
    void testUpdate_OwnerCancel() {
        // Arrange
        testRequest.setStatus(AppointmentStatus.CANCELED_BY_OWNER);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));
        when(appointmentRepository.saveAndFlush(any(AppointmentEntity.class))).thenReturn(testAppointment);
        doNothing().when(permissionValidator).validateForGet(any());
        doNothing().when(permissionValidator).validateForUpdate(any());
        doNothing().when(permissionValidator).validateForOwnerCancel(any());
        doNothing().when(validator).validateForUpdate(any(), any());
        doNothing().when(merger).mergeUpdate(any(), any());

        // Act
        appointmentService.update(1L, testRequest);

        // Assert
        verify(permissionValidator).validateForOwnerCancel(testAppointment);
    }

    // ===== CANCEL BY OWNER Tests =====

    @Test
    @DisplayName("Should cancel appointment by owner successfully")
    void testCancelByOwner_Success() {
        // Arrange
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));
        when(appointmentRepository.saveAndFlush(any(AppointmentEntity.class))).thenReturn(testAppointment);
        doNothing().when(permissionValidator).validateForGet(any());
        doNothing().when(permissionValidator).validateForOwnerCancel(any());
        doNothing().when(validator).validateForUpdate(any(), any());
        doNothing().when(merger).mergeUpdate(any(), any());

        // Act
        AppointmentEntity result = appointmentService.cancelByOwner(1L);

        // Assert
        assertNotNull(result);
        verify(permissionValidator).validateForOwnerCancel(testAppointment);
        verify(validator).validateForUpdate(any(AppointmentRequest.class), eq(testAppointment));
        verify(appointmentRepository).saveAndFlush(testAppointment);
    }

    // ===== CANCEL BY CLINIC Tests =====

    @Test
    @DisplayName("Should cancel appointment by clinic successfully")
    void testCancelByClinic_Success() {
        // Arrange
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));
        when(appointmentRepository.saveAndFlush(any(AppointmentEntity.class))).thenReturn(testAppointment);
        doNothing().when(permissionValidator).validateForGet(any());
        doNothing().when(permissionValidator).validateForClinicCancel(any());
        doNothing().when(validator).validateForUpdate(any(), any());
        doNothing().when(merger).mergeUpdate(any(), any());

        // Act
        AppointmentEntity result = appointmentService.cancelByClinic(1L);

        // Assert
        assertNotNull(result);
        verify(permissionValidator).validateForClinicCancel(testAppointment);
        verify(validator).validateForUpdate(any(AppointmentRequest.class), eq(testAppointment));
        verify(appointmentRepository).saveAndFlush(testAppointment);
    }

    // ===== MARK COMPLETED Tests =====

    @Test
    @DisplayName("Should mark appointment as completed successfully")
    void testMarkCompleted_Success() {
        // Arrange
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));
        when(appointmentRepository.saveAndFlush(any(AppointmentEntity.class))).thenReturn(testAppointment);
        doNothing().when(permissionValidator).validateForGet(any());
        doNothing().when(permissionValidator).validateForUpdate(any());
        doNothing().when(validator).validateForUpdate(any(), any());
        doNothing().when(merger).mergeUpdate(any(), any());

        // Act
        AppointmentEntity result = appointmentService.markCompleted(1L);

        // Assert
        assertNotNull(result);
        verify(permissionValidator).validateForUpdate(testAppointment);
        verify(appointmentRepository).saveAndFlush(testAppointment);
    }

    // ===== RESCHEDULE Tests =====

    @Test
    @DisplayName("Should reschedule appointment successfully")
    void testReschedule_Success() {
        // Arrange
        Long newTimeSlotId = 2L;
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));
        when(appointmentRepository.saveAndFlush(any(AppointmentEntity.class))).thenReturn(testAppointment);
        doNothing().when(permissionValidator).validateForGet(any());
        doNothing().when(permissionValidator).validateForUpdate(any());
        doNothing().when(validator).validateForUpdate(any(), any());
        doNothing().when(merger).mergeUpdate(any(), any());

        // Act
        AppointmentEntity result = appointmentService.reschedule(1L, newTimeSlotId);

        // Assert
        assertNotNull(result);
        verify(appointmentRepository).saveAndFlush(testAppointment);
    }

    @Test
    @DisplayName("Should throw ValidationException when rescheduling with null time slot id")
    void testReschedule_NullTimeSlotId() {
        // Act & Assert
        assertThrows(ValidationException.class, () -> appointmentService.reschedule(1L, null));
        verify(appointmentRepository, never()).saveAndFlush(any());
    }

    // ===== GET BY PET ID Tests =====

    @Test
    @DisplayName("Should get appointments by pet id successfully")
    void testGetByPetId_Success() {
        // Arrange
        List<AppointmentEntity> appointments = List.of(testAppointment);
        when(appointmentRepository.findByPetId(1L)).thenReturn(appointments);
        doNothing().when(permissionValidator).validateForGet(any());

        // Act
        List<AppointmentEntity> result = appointmentService.getByPetId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(appointmentRepository).findByPetId(1L);
    }

    @Test
    @DisplayName("Should filter out appointments without permission")
    void testGetByPetId_FilterUnauthorized() {
        // Arrange
        List<AppointmentEntity> appointments = List.of(testAppointment);
        when(appointmentRepository.findByPetId(1L)).thenReturn(appointments);
        doThrow(new RuntimeException("Forbidden")).when(permissionValidator).validateForGet(any());

        // Act
        List<AppointmentEntity> result = appointmentService.getByPetId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(appointmentRepository).findByPetId(1L);
    }

    // ===== GET BY OWNER ID Tests =====

    @Test
    @DisplayName("Should get appointments by owner id successfully")
    void testGetByOwnerId_Success() {
        // Arrange
        List<AppointmentEntity> appointments = List.of(testAppointment);
        when(appointmentRepository.findByPetOwnerId(1L)).thenReturn(appointments);
        doNothing().when(permissionValidator).validateForGet(any());

        // Act
        List<AppointmentEntity> result = appointmentService.getByOwnerId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(appointmentRepository).findByPetOwnerId(1L);
    }

    // ===== GET BY VETERINARIAN ID Tests =====

    @Test
    @DisplayName("Should get appointments by veterinarian id successfully")
    void testGetByVeterinarianId_Success() {
        // Arrange
        List<AppointmentEntity> appointments = List.of(testAppointment);
        when(appointmentRepository.findByTimeSlotVeterinarianId(1L)).thenReturn(appointments);
        doNothing().when(permissionValidator).validateForGet(any());

        // Act
        List<AppointmentEntity> result = appointmentService.getByVeterinarianId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(appointmentRepository).findByTimeSlotVeterinarianId(1L);
    }

    // ===== GET BY VETERINARIAN IN RANGE Tests =====

    @Test
    @DisplayName("Should get appointments by veterinarian in date range successfully")
    void testGetByVeterinarianInRange_Success() {
        // Arrange
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = LocalDateTime.now().plusDays(7);
        List<AppointmentEntity> appointments = List.of(testAppointment);
        when(appointmentRepository.findByTimeSlotVeterinarianIdAndTimeSlotStartTimeBetweenOrderByTimeSlotStartTime(1L, from, to))
                .thenReturn(appointments);
        doNothing().when(permissionValidator).validateForGet(any());

        // Act
        List<AppointmentEntity> result = appointmentService.getByVeterinarianInRange(1L, from, to);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(appointmentRepository).findByTimeSlotVeterinarianIdAndTimeSlotStartTimeBetweenOrderByTimeSlotStartTime(1L, from, to);
    }

    // ===== GET BY STATUS Tests =====

    @Test
    @DisplayName("Should get appointments by status successfully")
    void testGetByStatus_Success() {
        // Arrange
        List<AppointmentEntity> appointments = List.of(testAppointment);
        when(appointmentRepository.findByStatus(AppointmentStatus.SCHEDULED)).thenReturn(appointments);
        doNothing().when(permissionValidator).validateForGet(any());

        // Act
        List<AppointmentEntity> result = appointmentService.getByStatus(AppointmentStatus.SCHEDULED);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(appointmentRepository).findByStatus(AppointmentStatus.SCHEDULED);
    }

    // ===== GET BY ID Tests =====

    @Test
    @DisplayName("Should get appointment by id successfully")
    void testGetById_Success() {
        // Arrange
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));
        doNothing().when(permissionValidator).validateForGet(any());

        // Act
        AppointmentEntity result = appointmentService.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(appointmentRepository).findById(1L);
        verify(permissionValidator).validateForGet(testAppointment);
    }

    @Test
    @DisplayName("Should throw NotFoundException when appointment not found")
    void testGetById_NotFound() {
        // Arrange
        when(appointmentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> appointmentService.getById(999L));
        verify(appointmentRepository).findById(999L);
    }

    // ===== DELETE Tests =====

    @Test
    @DisplayName("Should delete appointment by id successfully")
    void testDeleteById_Success() {
        // Arrange
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));
        doNothing().when(permissionValidator).validateForGet(any());
        doNothing().when(permissionValidator).validateForDelete(any());
        doNothing().when(validator).validateForDelete(any());
        doNothing().when(appointmentRepository).deleteById(1L);

        // Act
        AppointmentEntity result = appointmentService.deleteById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(appointmentRepository).deleteById(1L);
    }

    // ===== GET ALL Tests =====

    @Test
    @DisplayName("Should get all appointments successfully")
    void testGetAll_Success() {
        // Arrange
        List<AppointmentEntity> appointments = List.of(testAppointment);
        when(appointmentRepository.findAll()).thenReturn(appointments);
        doNothing().when(permissionValidator).validateForGetAll();

        // Act
        List<AppointmentEntity> result = appointmentService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(appointmentRepository).findAll();
        verify(permissionValidator).validateForGetAll();
    }
}
