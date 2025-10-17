package ua.edu.ukma.objectanalysis.openvet.validator.data;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.ScheduleEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.appointment.ScheduleRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.ConflictException;
import ua.edu.ukma.objectanalysis.openvet.exception.ValidationException;
import ua.edu.ukma.objectanalysis.openvet.repository.appointment.ScheduleRepository;

import java.time.LocalTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ScheduleValidator extends BaseValidator<ScheduleEntity, ScheduleRequest> {

    private final ScheduleRepository scheduleRepository;

    @Override
    public void validateForCreate(ScheduleRequest request) {
        super.validateForCreate(request);
        if (request == null) { return; }
        checkTimes(request.getStartTime(), request.getEndTime(), request.getBreakStartTime(), request.getBreakEndTime());
        boolean exists = scheduleRepository.existsByVeterinarianIdAndDayOfWeek(request.getVeterinarianId(), request.getDayOfWeek());
        if (exists) {
            throw new ConflictException("Schedule already exists for veterinarian on this day");
        }
    }

    @Override
    public void validateForUpdate(ScheduleRequest request, ScheduleEntity entity) {
        super.validateForUpdate(request, entity);
        if (request == null || entity == null) { return; }

        LocalTime start = request.getStartTime() != null ? request.getStartTime() : entity.getStartTime();
        LocalTime end = request.getEndTime() != null ? request.getEndTime() : entity.getEndTime();
        LocalTime brStart = request.getBreakStartTime() != null ? request.getBreakStartTime() : entity.getBreakStartTime();
        LocalTime brEnd = request.getBreakEndTime() != null ? request.getBreakEndTime() : entity.getBreakEndTime();
        checkTimes(start, end, brStart, brEnd);

        Long vetId = request.getVeterinarianId() != null ? request.getVeterinarianId() : entity.getVeterinarian().getId();
        java.time.DayOfWeek day = request.getDayOfWeek() != null ? request.getDayOfWeek() : entity.getDayOfWeek();
        Optional<ScheduleEntity> existing = scheduleRepository.findByVeterinarianIdAndDayOfWeek(vetId, day);
        if (existing.isPresent() && !existing.get().getId().equals(entity.getId())) {
            throw new ConflictException("Schedule already exists for veterinarian on this day");
        }
    }

    private void checkTimes(LocalTime start, LocalTime end, LocalTime breakStart, LocalTime breakEnd) {
        if (start == null || end == null) {
            throw new ValidationException("Start and end time must be provided");
        }
        if (!start.isBefore(end)) {
            throw new ValidationException("Start time must be before end time");
        }
        if ((breakStart == null) != (breakEnd == null)) {
            throw new ValidationException("Both break start and break end must be provided together");
        }
        if (breakStart != null) {
            if (!breakStart.isBefore(breakEnd)) {
                throw new ValidationException("Break start must be before break end");
            }
            if (breakStart.isBefore(start) || breakEnd.isAfter(end)) {
                throw new ValidationException("Break must be within working hours");
            }
        }
    }
}

