package ua.edu.ukma.objectanalysis.openvet.repository.appointment;

import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.ScheduleEntity;
import ua.edu.ukma.objectanalysis.openvet.repository.BaseRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends BaseRepository<ScheduleEntity, Long> {
    List<ScheduleEntity> findByVeterinarianId(Long veterinarianId);
    List<ScheduleEntity> findByDayOfWeek(DayOfWeek dayOfWeek);
    Optional<ScheduleEntity> findByVeterinarianIdAndDayOfWeek(Long veterinarianId, DayOfWeek dayOfWeek);

    boolean existsByVeterinarianIdAndDayOfWeek(Long veterinarianId, DayOfWeek dayOfWeek);
}
