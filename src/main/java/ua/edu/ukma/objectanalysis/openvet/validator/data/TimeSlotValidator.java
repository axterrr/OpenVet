package ua.edu.ukma.objectanalysis.openvet.validator.data;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.TimeSlotEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.TimeSlotStatus;
import ua.edu.ukma.objectanalysis.openvet.dto.appointment.TimeSlotRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.ConflictException;
import ua.edu.ukma.objectanalysis.openvet.exception.ValidationException;
import ua.edu.ukma.objectanalysis.openvet.repository.appointment.TimeSlotRepository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TimeSlotValidator extends BaseValidator<TimeSlotEntity, TimeSlotRequest> {

    private final TimeSlotRepository timeSlotRepository;

    @Override
    public void validateForCreate(TimeSlotRequest request) {
        super.validateForCreate(request);
        if (request == null) { return; }

        checkTimeValidity(request.getStartTime(), request.getEndTime());
        ensureNoOverlap(null, request.getVeterinarianId(), request.getStartTime(), request.getEndTime());
    }

    @Override
    public void validateForUpdate(TimeSlotRequest request, TimeSlotEntity entity) {
        super.validateForUpdate(request, entity);
        if (request == null) { return; }

        // TODO: Can we partially update a time slot? Like only change the start/end time? Status?
        LocalDateTime start = request.getStartTime() != null ? request.getStartTime() : entity.getStartTime();
        LocalDateTime end = request.getEndTime() != null ? request.getEndTime() : entity.getEndTime();
        Long vetId = request.getVeterinarianId() != null ? request.getVeterinarianId() : entity.getVeterinarian().getId();
        checkTimeValidity(start, end);
        ensureNoOverlap(entity.getId(), vetId, start, end);

        if (entity.getStatus() == TimeSlotStatus.BOOKED && (request.getStartTime() != null || request.getEndTime() != null)) {
            throw new ConflictException("Cannot update a booked time slot");
        }
    }

    @Override
    public void validateForDelete(TimeSlotEntity entity) {
        if (entity.getStatus() == TimeSlotStatus.BOOKED || entity.getAppointment() != null) {
            throw new ConflictException("Cannot delete a booked time slot");
        }
    }

    private void checkTimeValidity(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new ValidationException("Start and end time must be provided");
        }
        if (!start.isBefore(end)) {
            throw new ValidationException("Start time must be before end time");
        }
    }

    /*
     * Verifies that there are no overlapping time slots for the given veterinarian.
     * If excludeId is provided, that time slot is excluded from the check (useful for updates).
     */
    private void ensureNoOverlap(Long excludeId, Long veterinarianId, LocalDateTime start, LocalDateTime end) {
        boolean overlaps;
        if (excludeId == null) {
            overlaps = timeSlotRepository.existOverlaps(veterinarianId, end, start);
        } else {
            overlaps = timeSlotRepository.existOverlapsWithExclude(veterinarianId, end, start, excludeId);
        }
        if (overlaps) {
            throw new ConflictException("Time slot overlaps with an existing slot");
        }
    }
}
