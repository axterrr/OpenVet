package ua.edu.ukma.objectanalysis.openvet.merger.examination;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.examination.PrescriptionEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.examination.PrescriptionRequest;
import ua.edu.ukma.objectanalysis.openvet.merger.BaseMerger;

import static ua.edu.ukma.objectanalysis.openvet.merger.MergerUtils.ifNotNull;

@Component
@RequiredArgsConstructor
public class PrescriptionMerger implements BaseMerger<PrescriptionEntity, PrescriptionRequest> {

    @Override
    public void mergeCreate(PrescriptionEntity entity, PrescriptionRequest request) {
        if (request == null || entity == null) { return; }
        commonMerge(entity, request);
    }

    @Override
    public void mergeUpdate(PrescriptionEntity entity, PrescriptionRequest request) {
        if (request == null || entity == null) { return; }
        commonMerge(entity, request);
    }

    private void commonMerge(PrescriptionEntity entity, PrescriptionRequest request) {
        ifNotNull(request.getMedicineName(), entity::setMedicationName);
        ifNotNull(request.getDosage(), entity::setDosage);
        ifNotNull(request.getFrequency(), entity::setFrequency);
        ifNotNull(request.getDuration(), entity::setDuration);
        ifNotNull(request.getInstructions(), entity::setInstructions);
    }
}
