package ua.edu.ukma.objectanalysis.openvet.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.ScheduleEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.appointment.ScheduleRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.NotFoundException;
import ua.edu.ukma.objectanalysis.openvet.merger.BaseMerger;
import ua.edu.ukma.objectanalysis.openvet.repository.BaseRepository;
import ua.edu.ukma.objectanalysis.openvet.repository.appointment.ScheduleRepository;
import ua.edu.ukma.objectanalysis.openvet.validator.data.ScheduleValidator;
import ua.edu.ukma.objectanalysis.openvet.validator.permission.SchedulePermissionValidator;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ScheduleService extends BaseService<ScheduleEntity, ScheduleRequest, Long> {

    private final ScheduleRepository scheduleRepository;

    private final SchedulePermissionValidator permissionValidator;

    public ScheduleService(
        BaseRepository<ScheduleEntity, Long> repository,
        BaseMerger<ScheduleEntity, ScheduleRequest> merger,
        ScheduleRepository scheduleRepository,
        SchedulePermissionValidator permissionValidator,
        ScheduleValidator validator
    ) {
        super(repository, merger, validator, permissionValidator);
        this.scheduleRepository = scheduleRepository;
        this.permissionValidator = permissionValidator;
        this.validator = validator;
    }

    @Override
    protected ScheduleEntity newEntity() {
        return new ScheduleEntity();
    }

    public List<ScheduleEntity> getForVeterinarian(Long veterinarianId) {
        List<ScheduleEntity> entity = scheduleRepository.findByVeterinarianId(veterinarianId);
        permissionValidator.validateForMultiple(entity);
        return entity;
    }

    public Map<DayOfWeek, List<ScheduleEntity>> getForVeterinarianMap(Long veterinarianId) {
        List<ScheduleEntity> all = scheduleRepository.findByVeterinarianId(veterinarianId);
        permissionValidator.validateForMultiple(all);
        Map<DayOfWeek, List<ScheduleEntity>> map = new HashMap<>();
        for (DayOfWeek d : DayOfWeek.values()) { map.put(d, new ArrayList<>()); }
        for (ScheduleEntity sch : all) {
            try {
                permissionValidator.validateForGet(sch);
                DayOfWeek d = sch.getDayOfWeek();
                if (d != null) { map.get(d).add(sch); }
            }
            catch (Exception ignored) {}
        }
        return map;
    }

    public List<ScheduleEntity> findByDayOfWeek(DayOfWeek dayOfWeek) {
        List<ScheduleEntity> all = scheduleRepository.findByDayOfWeek(dayOfWeek);
        List<ScheduleEntity> allowed = new ArrayList<>();
        for (ScheduleEntity sch : all) {
            try {
                permissionValidator.validateForGet(sch); allowed.add(sch);
            }
            catch (Exception ignored) {}
        }
        return allowed;
    }

    public ScheduleEntity getByVeterinarianAndDay(Long veterinarianId, DayOfWeek dayOfWeek) {
        Optional<ScheduleEntity> schedulOpt = scheduleRepository.findByVeterinarianIdAndDayOfWeek(veterinarianId, dayOfWeek);
        ScheduleEntity entity = schedulOpt.orElseThrow(()
                -> new NotFoundException("Schedule not found for veterinarian at " + dayOfWeek));
        permissionValidator.validateForGet(entity);
        return entity;
    }
}

