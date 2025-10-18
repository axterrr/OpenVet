package ua.edu.ukma.objectanalysis.openvet.validator.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.ScheduleEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.VeterinarianEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.appointment.ScheduleRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.ConflictException;
import ua.edu.ukma.objectanalysis.openvet.exception.ValidationException;
import ua.edu.ukma.objectanalysis.openvet.repository.appointment.ScheduleRepository;

import java.lang.reflect.Field;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleValidatorTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private jakarta.validation.Validator validator;

    @InjectMocks
    private ScheduleValidator scheduleValidator;

    private ScheduleRequest scheduleRequest;
    private ScheduleEntity scheduleEntity;
    private VeterinarianEntity veterinarian;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        scheduleRequest = new ScheduleRequest();
        scheduleRequest.setVeterinarianId(1L);
        scheduleRequest.setDayOfWeek(DayOfWeek.MONDAY);
        scheduleRequest.setStartTime(LocalTime.of(9, 0));
        scheduleRequest.setEndTime(LocalTime.of(17, 0));

        veterinarian = new VeterinarianEntity();
        veterinarian.setId(1L);

        scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setVeterinarian(veterinarian);
        scheduleEntity.setDayOfWeek(DayOfWeek.MONDAY);
        scheduleEntity.setStartTime(LocalTime.of(9, 0));
        scheduleEntity.setEndTime(LocalTime.of(17, 0));

        when(validator.validate(any())).thenReturn(Collections.emptySet());
        Field validatorField = BaseValidator.class.getDeclaredField("validator");
        validatorField.setAccessible(true);
        validatorField.set(scheduleValidator, validator);
    }

    @Test
    void validateForCreate_ValidRequest_NoException() {
        when(scheduleRepository.existsByVeterinarianIdAndDayOfWeek(1L, DayOfWeek.MONDAY)).thenReturn(false);
        assertDoesNotThrow(() -> scheduleValidator.validateForCreate(scheduleRequest));
    }

    @Test
    void validateForCreate_NullRequest_NoException() {
        assertDoesNotThrow(() -> scheduleValidator.validateForCreate(null));
    }

    @Test
    void validateForCreate_ScheduleAlreadyExists_ThrowsConflictException() {
        when(scheduleRepository.existsByVeterinarianIdAndDayOfWeek(1L, DayOfWeek.MONDAY)).thenReturn(true);
        assertThrows(ConflictException.class, () -> scheduleValidator.validateForCreate(scheduleRequest));
    }

    @Test
    void validateForCreate_StartTimeAfterEndTime_ThrowsValidationException() {
        scheduleRequest.setStartTime(LocalTime.of(17, 0));
        scheduleRequest.setEndTime(LocalTime.of(9, 0));
        assertThrows(ValidationException.class, () -> scheduleValidator.validateForCreate(scheduleRequest));
    }

    @Test
    void validateForCreate_BreakStartWithoutBreakEnd_ThrowsValidationException() {
        scheduleRequest.setBreakStartTime(LocalTime.of(12, 0));
        assertThrows(ValidationException.class, () -> scheduleValidator.validateForCreate(scheduleRequest));
    }

    @Test
    void validateForCreate_BreakStartAfterBreakEnd_ThrowsValidationException() {
        scheduleRequest.setBreakStartTime(LocalTime.of(13, 0));
        scheduleRequest.setBreakEndTime(LocalTime.of(12, 0));
        assertThrows(ValidationException.class, () -> scheduleValidator.validateForCreate(scheduleRequest));
    }

    @Test
    void validateForCreate_BreakOutsideWorkingHours_ThrowsValidationException() {
        scheduleRequest.setBreakStartTime(LocalTime.of(8, 0));
        scheduleRequest.setBreakEndTime(LocalTime.of(9, 0));
        assertThrows(ValidationException.class, () -> scheduleValidator.validateForCreate(scheduleRequest));
    }

    @Test
    void validateForUpdate_ValidRequest_NoException() {
        when(scheduleRepository.findByVeterinarianIdAndDayOfWeek(1L, DayOfWeek.MONDAY)).thenReturn(Optional.of(scheduleEntity));
        assertDoesNotThrow(() -> scheduleValidator.validateForUpdate(scheduleRequest, scheduleEntity));
    }

    @Test
    void validateForUpdate_ScheduleConflictWithOther_ThrowsConflictException() {
        ScheduleEntity otherSchedule = new ScheduleEntity();
        otherSchedule.setId(2L);
        when(scheduleRepository.findByVeterinarianIdAndDayOfWeek(1L, DayOfWeek.MONDAY)).thenReturn(Optional.of(otherSchedule));
        assertThrows(ConflictException.class, () -> scheduleValidator.validateForUpdate(scheduleRequest, scheduleEntity));
    }
}
