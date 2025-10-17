package ua.edu.ukma.objectanalysis.openvet.repository.appointment;

import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.TimeSlotEntity;
import ua.edu.ukma.objectanalysis.openvet.repository.BaseRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TimeSlotRepository extends BaseRepository<TimeSlotEntity, Long> {
    Optional<TimeSlotEntity> findByVeterinarianId(Long veterinarianId);
    Optional<TimeSlotEntity> findByVeterinarianIdAndStartTime(Long veterinarian_id, LocalDateTime startTime);

    boolean existsByVeterinarianIdAndStartTime(Long veterinarian_id, LocalDateTime startTime);
    boolean existsByVeterinarianIdAndStartTimeBetween(Long veterinarianId, LocalDateTime start, LocalDateTime end);

    /*
     * If we were given an existing time slot with time range [start, end],
     * and have a new time range [newStart, newEnd],
     * than we should check if there is any existing slot that overlaps with the new time range.
     * New slot overlaps with existing slot if:
     *   start < newEnd AND end > newStart
     */
    boolean existsByVeterinarianIdAndStartTimeLessThanAndEndTimeGreaterThan(Long veterinarianId, LocalDateTime newEnd, LocalDateTime newStart);

    // Same as above, but exclude a specific slot by ID (for updates)
    boolean existsByVeterinarianIdAndIdNotAndStartTimeLessThanAndEndTimeGreaterThan(Long veterinarianId, Long excludeId, LocalDateTime newEnd, LocalDateTime newStart);

    // Adjacency for merge
    Optional<TimeSlotEntity> findByVeterinarianIdAndEndTime(Long veterinarianId, LocalDateTime endTime);

    // Get all slots between [from, to] ordered by start time
    List<TimeSlotEntity> findByVeterinarianIdAndStartTimeGreaterThanEqualAndEndTimeLessThanEqualOrderByStartTime(Long veterinarianId, LocalDateTime from, LocalDateTime to);

    // Delegate method for checking if time overlaps with any existing slot
    default boolean existOverlaps(Long veterinarianId, LocalDateTime newStart, LocalDateTime newEnd) {
        return existsByVeterinarianIdAndStartTimeLessThanAndEndTimeGreaterThan(veterinarianId, newEnd, newStart);
    }

    // Delegate method for time slots in time range
    default List<TimeSlotEntity> findInTimeRange(Long veterinarianId, LocalDateTime from, LocalDateTime to) {
        return findByVeterinarianIdAndStartTimeGreaterThanEqualAndEndTimeLessThanEqualOrderByStartTime(veterinarianId, from, to);
    }

    // Delegate method for checking if time overlaps with any existing slot (with exclude)
    default boolean existOverlapsWithExclude(Long veterinarianId, LocalDateTime newStart, LocalDateTime newEnd, Long excludeId) {
        return existsByVeterinarianIdAndIdNotAndStartTimeLessThanAndEndTimeGreaterThan(veterinarianId, excludeId, newEnd, newStart);
    }
}
