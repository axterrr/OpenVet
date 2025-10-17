package ua.edu.ukma.objectanalysis.openvet.merger;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.AppointmentEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.TimeSlotEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PetEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.AppointmentStatus;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.TimeSlotStatus;
import ua.edu.ukma.objectanalysis.openvet.dto.appointment.AppointmentRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.NotFoundException;
import ua.edu.ukma.objectanalysis.openvet.repository.appointment.TimeSlotRepository;
import ua.edu.ukma.objectanalysis.openvet.repository.pet.PetRepository;

import static ua.edu.ukma.objectanalysis.openvet.merger.MergerUtils.ifNotNull;

@Component
@RequiredArgsConstructor
public class AppointmentMerger implements BaseMerger<AppointmentEntity, AppointmentRequest> {

    private final PetRepository petRepository;
    private final TimeSlotRepository timeSlotRepository;

    @Override
    public void mergeCreate(AppointmentEntity entity, AppointmentRequest request) {
        if (entity == null || request == null) { return; }
        commonMerge(entity, request);
        if (entity.getStatus() == null) {
            entity.setStatus(AppointmentStatus.SCHEDULED);
        }
    }

    @Override
    public void mergeUpdate(AppointmentEntity entity, AppointmentRequest request) {
        if (entity == null || request == null) { return; }

        if (request.getTimeSlotId() != null) {
            TimeSlotEntity current = entity.getTimeSlot();
            if (current == null || !request.getTimeSlotId().equals(current.getId())) {
                if (current != null) {
                    current.setAppointment(null);
                    current.setStatus(TimeSlotStatus.AVAILABLE);
                }
                TimeSlotEntity newSlot = timeSlotRepository.findById(request.getTimeSlotId())
                    .orElseThrow(() -> new NotFoundException("Time slot not found"));
                entity.setTimeSlot(newSlot);
                newSlot.setAppointment(entity);
                newSlot.setStatus(TimeSlotStatus.BOOKED);
            }
        }

        if (request.getStatus() != null) {
            entity.setStatus(request.getStatus());
            switch (request.getStatus()) {
                case CANCELED_BY_OWNER, CANCELED_BY_CLINIC, NO_SHOW -> {
                    TimeSlotEntity slot = entity.getTimeSlot();
                    if (slot != null) {
                        slot.setAppointment(null);
                        slot.setStatus(TimeSlotStatus.AVAILABLE);
                    }
                }
                default -> {
                    // Trying to breathe this code...
                }
            }
        }

        ifNotNull(request.getReasonForVisit(), entity::setReasonForVisit);
        ifNotNull(request.getNotes(), entity::setNotes);

        if (request.getPetId() != null) {
            PetEntity pet = petRepository.findById(request.getPetId())
                .orElseThrow(() -> new NotFoundException("Pet not found"));
            entity.setPet(pet);
        }
    }

    private void commonMerge(AppointmentEntity entity, AppointmentRequest request) {
        if (request.getPetId() != null) {
            PetEntity pet = petRepository.findById(request.getPetId())
                .orElseThrow(() -> new NotFoundException("Pet not found"));
            entity.setPet(pet);
        }
        if (request.getTimeSlotId() != null) {
            TimeSlotEntity slot = timeSlotRepository.findById(request.getTimeSlotId())
                .orElseThrow(() -> new NotFoundException("Time slot not found"));
            entity.setTimeSlot(slot);
            slot.setAppointment(entity);
            slot.setStatus(TimeSlotStatus.BOOKED);
        }
        ifNotNull(request.getStatus(), entity::setStatus);
        ifNotNull(request.getReasonForVisit(), entity::setReasonForVisit);
        ifNotNull(request.getNotes(), entity::setNotes);
    }
}

