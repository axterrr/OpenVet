package ua.edu.ukma.objectanalysis.openvet.merger;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.ScheduleEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.VeterinarianEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.appointment.ScheduleRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.NotFoundException;
import ua.edu.ukma.objectanalysis.openvet.repository.user.VeterinarianRepository;

import static ua.edu.ukma.objectanalysis.openvet.merger.MergerUtils.ifNotNull;

@Component
@RequiredArgsConstructor
public class ScheduleMerger implements BaseMerger<ScheduleEntity, ScheduleRequest> {

    private final VeterinarianRepository veterinarianRepository;

    @Override
    public void mergeCreate(ScheduleEntity entity, ScheduleRequest request) {
        if (entity == null || request == null) { return; }
        VeterinarianEntity vet = veterinarianRepository.findById(request.getVeterinarianId())
            .orElseThrow(() -> new NotFoundException("Veterinarian not found"));
        entity.setVeterinarian(vet);
        commonMerge(entity, request);
    }

    @Override
    public void mergeUpdate(ScheduleEntity entity, ScheduleRequest request) {
        if (entity == null || request == null) { return; }
        if (request.getVeterinarianId() != null) {
            VeterinarianEntity vet = veterinarianRepository.findById(request.getVeterinarianId())
                .orElseThrow(() -> new NotFoundException("Veterinarian not found"));
            entity.setVeterinarian(vet);
        }
        commonMerge(entity, request);
    }

    private void commonMerge(ScheduleEntity entity, ScheduleRequest request) {
        ifNotNull(request.getDayOfWeek(), entity::setDayOfWeek);
        ifNotNull(request.getStartTime(), entity::setStartTime);
        ifNotNull(request.getEndTime(), entity::setEndTime);
        ifNotNull(request.getBreakStartTime(), entity::setBreakStartTime);
        ifNotNull(request.getBreakEndTime(), entity::setBreakEndTime);
    }
}

