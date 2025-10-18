package ua.edu.ukma.objectanalysis.openvet.validator.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.AppointmentEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.TimeSlotEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.VeterinarianEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.TimeSlotStatus;
import ua.edu.ukma.objectanalysis.openvet.dto.appointment.TimeSlotRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.ConflictException;
import ua.edu.ukma.objectanalysis.openvet.exception.ValidationException;
import ua.edu.ukma.objectanalysis.openvet.repository.appointment.TimeSlotRepository;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeSlotValidatorTest {

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @Mock
    private jakarta.validation.Validator validator;

    @InjectMocks
    private TimeSlotValidator timeSlotValidator;

    private TimeSlotRequest timeSlotRequest;
    private TimeSlotEntity timeSlotEntity;
    private VeterinarianEntity veterinarian;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        timeSlotRequest = new TimeSlotRequest();
        timeSlotRequest.setVeterinarianId(1L);
        timeSlotRequest.setStartTime(LocalDateTime.now().plusDays(1));
        timeSlotRequest.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));

        veterinarian = new VeterinarianEntity();
        veterinarian.setId(1L);

        timeSlotEntity = new TimeSlotEntity();
        timeSlotEntity.setId(1L);
        timeSlotEntity.setVeterinarian(veterinarian);
        timeSlotEntity.setStartTime(LocalDateTime.now().plusDays(1));
        timeSlotEntity.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));
        timeSlotEntity.setStatus(TimeSlotStatus.AVAILABLE);

        Field validatorField = BaseValidator.class.getDeclaredField("validator");
        validatorField.setAccessible(true);
        validatorField.set(timeSlotValidator, validator);
    }

    @Test
    void validateForCreate_ValidRequest_NoException() {
        when(validator.validate(any())).thenReturn(Collections.emptySet());
        when(timeSlotRepository.existOverlaps(1L, timeSlotRequest.getEndTime(), timeSlotRequest.getStartTime())).thenReturn(false);
        assertDoesNotThrow(() -> timeSlotValidator.validateForCreate(timeSlotRequest));
    }

    @Test
    void validateForCreate_OverlappingSlot_ThrowsConflictException() {
        when(validator.validate(any())).thenReturn(Collections.emptySet());
        when(timeSlotRepository.existOverlaps(1L, timeSlotRequest.getEndTime(), timeSlotRequest.getStartTime())).thenReturn(true);
        assertThrows(ConflictException.class, () -> timeSlotValidator.validateForCreate(timeSlotRequest));
    }

    @Test
    void validateForCreate_StartTimeAfterEndTime_ThrowsValidationException() {
        when(validator.validate(any())).thenReturn(Collections.emptySet());
        timeSlotRequest.setStartTime(LocalDateTime.now().plusHours(2));
        timeSlotRequest.setEndTime(LocalDateTime.now().plusHours(1));
        assertThrows(ValidationException.class, () -> timeSlotValidator.validateForCreate(timeSlotRequest));
    }

    @Test
    void validateForUpdate_BookedSlotTimeChange_ThrowsConflictException() {
        when(validator.validate(any())).thenReturn(Collections.emptySet());
        timeSlotEntity.setStatus(TimeSlotStatus.BOOKED);
        assertThrows(ConflictException.class, () -> timeSlotValidator.validateForUpdate(timeSlotRequest, timeSlotEntity));
    }

    @Test
    void validateForDelete_BookedSlot_ThrowsConflictException() {
        timeSlotEntity.setStatus(TimeSlotStatus.BOOKED);
        assertThrows(ConflictException.class, () -> timeSlotValidator.validateForDelete(timeSlotEntity));
    }

    @Test
    void validateForDelete_BookedSlotWithAppointment_ThrowsConflictException() {
        timeSlotEntity.setAppointment(new AppointmentEntity());
        assertThrows(ConflictException.class, () -> timeSlotValidator.validateForDelete(timeSlotEntity));
    }

    @Test
    void validateForDelete_AvailableSlot_NoException() {
        assertDoesNotThrow(() -> timeSlotValidator.validateForDelete(timeSlotEntity));
    }
}
