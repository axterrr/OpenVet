package ua.edu.ukma.objectanalysis.openvet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.AppointmentEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.TimeSlotEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.TimeSlotStatus;
import ua.edu.ukma.objectanalysis.openvet.dto.appointment.TimeSlotRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.ConflictException;
import ua.edu.ukma.objectanalysis.openvet.exception.NotFoundException;
import ua.edu.ukma.objectanalysis.openvet.repository.appointment.AppointmentRepository;
import ua.edu.ukma.objectanalysis.openvet.repository.appointment.TimeSlotRepository;
import ua.edu.ukma.objectanalysis.openvet.repository.user.VeterinarianRepository;
import ua.edu.ukma.objectanalysis.openvet.validator.data.TimeSlotValidator;
import ua.edu.ukma.objectanalysis.openvet.validator.permission.TimeSlotPermissionValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TimeSlotService extends BaseService<TimeSlotEntity, TimeSlotRequest, Long> {

    private final TimeSlotRepository timeSlotRepository;
    private final AppointmentRepository appointmentRepository;

    private final TimeSlotPermissionValidator permissionValidator;
    private final TimeSlotValidator validator;
    private final VeterinarianRepository veterinarianRepository;

    @Override
    protected TimeSlotEntity newEntity() {
        return new TimeSlotEntity();
    }

    // Method that unites all contiguous slots within the given time range for the specified veterinarian
    public int mergeContiguous(Long veterinarianId, LocalDateTime from, LocalDateTime to) {
        if (from.isAfter(to)) { throw new IllegalArgumentException("Start time must be before end time"); }
        if (!veterinarianRepository.existsById(veterinarianId)) { throw new NotFoundException("Veterinarian not found"); }

        List<TimeSlotEntity> slots = timeSlotRepository
            .findInTimeRange(veterinarianId, from, to);
        if (slots.isEmpty()) { return 0; }

        int mergedCount = 0;
        Iterator<TimeSlotEntity> it = slots.iterator();
        TimeSlotEntity current = it.next();
        permissionValidator.validateForUpdate(current);

        List<TimeSlotEntity> toDelete = new ArrayList<>();
        List<TimeSlotEntity> modifiedSurvivors = new ArrayList<>();

        while (it.hasNext()) {
            TimeSlotEntity next = it.next();
            permissionValidator.validateForUpdate(next);
            boolean mergeable = areSlotsMergeable(current, next);
            if (mergeable) {
                mergeSlotPair(current, next, toDelete);
                mergedCount++;
            } else {
                break;
            }
        }

        if (!toDelete.isEmpty()) {
            timeSlotRepository.save(current);
            timeSlotRepository.deleteAll(toDelete);
            timeSlotRepository.flush();
        }
        return mergedCount;
    }

    // Method that unites all provided slots with the same veterinarian and status
    public void mergeSlotsList(List<Long> slotsIds) {
        List<TimeSlotEntity> slots = new ArrayList<>();
        for (Long id : slotsIds) {
            TimeSlotEntity slot = timeSlotRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Time slot not found with id: " + id));

            slots.add(slot);
        }

        slots.sort(Comparator.comparing(TimeSlotEntity::getStartTime));
        Iterator<TimeSlotEntity> it = slots.iterator();
        TimeSlotEntity current = it.next();
        List<TimeSlotEntity> toDelete = new ArrayList<>();

        while (it.hasNext()) {
            permissionValidator.validateForUpdate(current);
            TimeSlotEntity next = it.next();
            boolean mergeable = areSlotsMergeable(current, next);
            if (mergeable) {
                mergeSlotPair(current, next, toDelete);
            } else {
                throw new ConflictException("Cannot merge slots with different statuses or veterinarians");
            }
        }

        timeSlotRepository.save(current);
        timeSlotRepository.deleteAll(toDelete);
        timeSlotRepository.flush();
    }

    public TimeSlotEntity linkAppointment(Long slotId, Long appointmentId) {
        TimeSlotEntity slot = timeSlotRepository.findById(slotId)
            .orElseThrow(() -> new NotFoundException("Time slot not found"));
        permissionValidator.validateForUpdate(slot);

        if (slot.getStatus() != TimeSlotStatus.AVAILABLE) {
            throw new ConflictException("Only AVAILABLE time slots can be booked");
        }

        AppointmentEntity appt = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new NotFoundException("Appointment not found"));
        if (appt.getTimeSlot() != null && !appt.getTimeSlot().getId().equals(slot.getId())) {
            throw new ConflictException("Appointment already linked to another time slot");
        }

        if (slot.getAppointment() != null && !slot.getAppointment().getId().equals(appt.getId())) {
            throw new ConflictException("Time slot already linked to a different appointment");
        }
        
        slot.setAppointment(appt);
        slot.setStatus(TimeSlotStatus.BOOKED);
        appt.setTimeSlot(slot);
        appointmentRepository.save(appt);
        return timeSlotRepository.saveAndFlush(slot);
    }

    public TimeSlotEntity unlinkAppointment(Long slotId) {
        TimeSlotEntity slot = timeSlotRepository.findById(slotId)
            .orElseThrow(() -> new NotFoundException("Time slot not found"));
        permissionValidator.validateForUpdate(slot);

        if (slot.getAppointment() == null) { return slot; }
        AppointmentEntity appt = appointmentRepository.findById(slot.getAppointment().getId())
            .orElseThrow(() -> new NotFoundException("Appointment not found"));

        appt.setTimeSlot(null);
        slot.setAppointment(null);
        if (slot.getStatus() == TimeSlotStatus.BOOKED) {
            slot.setStatus(TimeSlotStatus.AVAILABLE);
        }

        appointmentRepository.save(appt);
        return timeSlotRepository.saveAndFlush(slot);
    }

    // HELPER METHODS

    private boolean areSlotsMergeable(TimeSlotEntity current, TimeSlotEntity next) {
        return current.getAppointment() == null && next.getAppointment() == null
                && current.getStatus() != TimeSlotStatus.BOOKED
                && current.getStatus() == next.getStatus()
                && current.getVeterinarian().getId().equals(next.getVeterinarian().getId())
                && current.getEndTime().equals(next.getStartTime());
    }

    private void mergeSlotPair(TimeSlotEntity current, TimeSlotEntity next, List<TimeSlotEntity> toDelete) {
        current.setEndTime(next.getEndTime());
        toDelete.add(next);
    }
}
