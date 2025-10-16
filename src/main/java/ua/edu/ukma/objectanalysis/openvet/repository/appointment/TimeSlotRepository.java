package ua.edu.ukma.objectanalysis.openvet.repository.appointment;

import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.TimeSlotEntity;
import ua.edu.ukma.objectanalysis.openvet.repository.BaseRepository;

import java.time.LocalDateTime;

public interface TimeSlotRepository extends BaseRepository<TimeSlotEntity, Long> {
    TimeSlotEntity findByVeterinarianId(Long veterinarianId);
    TimeSlotEntity findByVeterinarianIdAndStartTime(Long veterinarian_id, LocalDateTime startTime);

    boolean existsByVeterinarianIdAndStartTime(Long veterinarian_id, LocalDateTime startTime);
    boolean existsByVeterinarianIdAndStartTimeBetween(Long veterinarianId, LocalDateTime start, LocalDateTime end);
}
