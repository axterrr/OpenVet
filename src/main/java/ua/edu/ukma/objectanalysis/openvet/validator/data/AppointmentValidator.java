package ua.edu.ukma.objectanalysis.openvet.validator.data;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.AppointmentEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.TimeSlotEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.AppointmentStatus;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.TimeSlotStatus;
import ua.edu.ukma.objectanalysis.openvet.dto.appointment.AppointmentRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.ConflictException;
import ua.edu.ukma.objectanalysis.openvet.exception.NotFoundException;
import ua.edu.ukma.objectanalysis.openvet.exception.ValidationException;
import ua.edu.ukma.objectanalysis.openvet.repository.appointment.TimeSlotRepository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AppointmentValidator extends BaseValidator<AppointmentEntity, AppointmentRequest> {

    private final TimeSlotRepository timeSlotRepository;

    @Override
    public void validateForCreate(AppointmentRequest request) {
        super.validateForCreate(request);

        if (request == null) { return; }
        if (request.getPetId() == null) {
            throw new ValidationException("Pet id is required");
        }
        if (request.getTimeSlotId() == null) {
            throw new ValidationException("Time slot id is required");
        }
        TimeSlotEntity slot = timeSlotRepository.findById(request.getTimeSlotId())
            .orElseThrow(() -> new NotFoundException("Time slot not found"));

        if (slot.getStatus() != TimeSlotStatus.AVAILABLE) {
            throw new ConflictException("Time slot is not available");
        }

        if (slot.getStartTime() != null && slot.getStartTime().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Cannot book a time slot in the past");
        }
    }

    @Override
    public void validateForUpdate(AppointmentRequest request, AppointmentEntity entity) {
        super.validateForUpdate(request, entity);
        if (request == null) { return; }

        if (request.getTimeSlotId() != null) {
            TimeSlotEntity newSlot = timeSlotRepository.findById(request.getTimeSlotId())
                .orElseThrow(() -> new NotFoundException("Time slot not found"));
            if (newSlot.getStatus() != TimeSlotStatus.AVAILABLE) {
                throw new ConflictException("New time slot is not available");
            }
        }

        if (request.getStatus() != null) {
            if (request.getStatus() == AppointmentStatus.COMPLETED) {
                TimeSlotEntity slot = entity.getTimeSlot();
                if (slot != null && slot.getEndTime() != null && slot.getEndTime().isAfter(LocalDateTime.now())) {
                    throw new ValidationException("Cannot mark appointment as completed before it ends");
                }
            }
        }
    }
}

