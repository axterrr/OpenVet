package ua.edu.ukma.objectanalysis.openvet.validator.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.AppointmentEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.TimeSlotEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.AppointmentStatus;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.TimeSlotStatus;
import ua.edu.ukma.objectanalysis.openvet.dto.appointment.AppointmentRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.ConflictException;
import ua.edu.ukma.objectanalysis.openvet.exception.NotFoundException;
import ua.edu.ukma.objectanalysis.openvet.exception.ValidationException;
import ua.edu.ukma.objectanalysis.openvet.repository.appointment.TimeSlotRepository;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentValidatorTest {

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @Mock
    private jakarta.validation.Validator validator;

    @InjectMocks
    private AppointmentValidator appointmentValidator;

    private AppointmentRequest appointmentRequest;
    private TimeSlotEntity timeSlot;
    private AppointmentEntity appointmentEntity;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        appointmentRequest = new AppointmentRequest();
        appointmentRequest.setPetId(1L);
        appointmentRequest.setTimeSlotId(1L);
        appointmentRequest.setReasonForVisit("Checkup");

        timeSlot = new TimeSlotEntity();
        timeSlot.setId(1L);
        timeSlot.setStatus(TimeSlotStatus.AVAILABLE);
        timeSlot.setStartTime(LocalDateTime.now().plusDays(1));
        timeSlot.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));

        appointmentEntity = new AppointmentEntity();
        appointmentEntity.setId(1L);
        appointmentEntity.setTimeSlot(timeSlot);

        when(validator.validate(any())).thenReturn(Collections.emptySet());
        Field validatorField = BaseValidator.class.getDeclaredField("validator");
        validatorField.setAccessible(true);
        validatorField.set(appointmentValidator, validator);
    }

    @Test
    void validateForCreate_ValidRequest_NoException() {
        when(timeSlotRepository.findById(1L)).thenReturn(Optional.of(timeSlot));
        assertDoesNotThrow(() -> appointmentValidator.validateForCreate(appointmentRequest));
    }

    @Test
    void validateForCreate_NullRequest_NoException() {
        assertDoesNotThrow(() -> appointmentValidator.validateForCreate(null));
    }

    @Test
    void validateForCreate_NullPetId_ThrowsValidationException() {
        appointmentRequest.setPetId(null);
        assertThrows(ValidationException.class, () -> appointmentValidator.validateForCreate(appointmentRequest));
    }

    @Test
    void validateForCreate_NullTimeSlotId_ThrowsValidationException() {
        appointmentRequest.setTimeSlotId(null);
        assertThrows(ValidationException.class, () -> appointmentValidator.validateForCreate(appointmentRequest));
    }

    @Test
    void validateForCreate_TimeSlotNotFound_ThrowsNotFoundException() {
        when(timeSlotRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> appointmentValidator.validateForCreate(appointmentRequest));
    }

    @Test
    void validateForCreate_TimeSlotNotAvailable_ThrowsConflictException() {
        timeSlot.setStatus(TimeSlotStatus.BOOKED);
        when(timeSlotRepository.findById(1L)).thenReturn(Optional.of(timeSlot));
        assertThrows(ConflictException.class, () -> appointmentValidator.validateForCreate(appointmentRequest));
    }

    @Test
    void validateForCreate_TimeSlotInThePast_ThrowsValidationException() {
        timeSlot.setStartTime(LocalDateTime.now().minusDays(1));
        when(timeSlotRepository.findById(1L)).thenReturn(Optional.of(timeSlot));
        assertThrows(ValidationException.class, () -> appointmentValidator.validateForCreate(appointmentRequest));
    }

    @Test
    void validateForUpdate_ValidRequest_NoException() {
        assertDoesNotThrow(() -> appointmentValidator.validateForUpdate(new AppointmentRequest(), appointmentEntity));
    }

    @Test
    void validateForUpdate_NullRequest_NoException() {
        assertDoesNotThrow(() -> appointmentValidator.validateForUpdate(null, appointmentEntity));
    }

    @Test
    void validateForUpdate_NewTimeSlotNotFound_ThrowsNotFoundException() {
        appointmentRequest.setTimeSlotId(2L);
        when(timeSlotRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> appointmentValidator.validateForUpdate(appointmentRequest, appointmentEntity));
    }

    @Test
    void validateForUpdate_NewTimeSlotNotAvailable_ThrowsConflictException() {
        TimeSlotEntity newTimeSlot = new TimeSlotEntity();
        newTimeSlot.setId(2L);
        newTimeSlot.setStatus(TimeSlotStatus.BOOKED);
        appointmentRequest.setTimeSlotId(2L);
        when(timeSlotRepository.findById(2L)).thenReturn(Optional.of(newTimeSlot));
        assertThrows(ConflictException.class, () -> appointmentValidator.validateForUpdate(appointmentRequest, appointmentEntity));
    }
}
