package ua.edu.ukma.objectanalysis.openvet.merger;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.TimeSlotEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.VeterinarianEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.TimeSlotStatus;
import ua.edu.ukma.objectanalysis.openvet.dto.appointment.TimeSlotRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.NotFoundException;
import ua.edu.ukma.objectanalysis.openvet.repository.user.VeterinarianRepository;

import static ua.edu.ukma.objectanalysis.openvet.merger.MergerUtils.ifNotNull;

@Component
@RequiredArgsConstructor
public class TimeSlotMerger implements BaseMerger<TimeSlotEntity, TimeSlotRequest> {

    private final VeterinarianRepository veterinarianRepository;

    @Override
    public void mergeCreate(TimeSlotEntity entity, TimeSlotRequest request) {
        if (entity == null || request == null) { return; }
        commonMerge(entity, request);
        if (entity.getStatus() == null) entity.setStatus(TimeSlotStatus.AVAILABLE);
    }

    @Override
    public void mergeUpdate(TimeSlotEntity entity, TimeSlotRequest request) {
        if (entity == null || request == null) { return; }
        commonMerge(entity, request);
    }

    private void commonMerge(TimeSlotEntity entity, TimeSlotRequest request) {
        if (request.getVeterinarianId() != null) {
            VeterinarianEntity vet = veterinarianRepository.findById(request.getVeterinarianId())
                .orElseThrow(() -> new NotFoundException("Veterinarian not found"));
            entity.setVeterinarian(vet);
        }
        ifNotNull(request.getStartTime(), entity::setStartTime);
        ifNotNull(request.getEndTime(), entity::setEndTime);
        ifNotNull(request.getStatus(), entity::setStatus);
    }
}
