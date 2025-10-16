package ua.edu.ukma.objectanalysis.openvet.repository.appointment;

import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.ScheduleEntity;
import ua.edu.ukma.objectanalysis.openvet.repository.BaseRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface ScheduleRepository extends BaseRepository<ScheduleEntity, Long> {
    ScheduleEntity findByVeterinarianId(Long veterinarianId);
    List<ScheduleEntity> findByDayOfWeek(DayOfWeek dayOfWeek);
    List<ScheduleEntity> findByVeterinarianIdAndDayOfWeek(Long veterinarianId, DayOfWeek dayOfWeek);
}
