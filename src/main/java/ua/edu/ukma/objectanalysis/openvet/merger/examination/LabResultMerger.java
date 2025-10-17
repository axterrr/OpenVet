package ua.edu.ukma.objectanalysis.openvet.merger.examination;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.examination.LabResultEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.examination.LabResultRequest;
import ua.edu.ukma.objectanalysis.openvet.merger.BaseMerger;

import static ua.edu.ukma.objectanalysis.openvet.merger.MergerUtils.ifNotNull;

@Component
@RequiredArgsConstructor
public class LabResultMerger implements BaseMerger<LabResultEntity, LabResultRequest> {

    @Override
    public void mergeCreate(LabResultEntity entity, LabResultRequest request) {
        if (request == null || entity == null) { return; }
        commonMerge(entity, request);
    }

    @Override
    public void mergeUpdate(LabResultEntity entity, LabResultRequest request) {
        if (request == null || entity == null) { return; }
        commonMerge(entity, request);
    }

    private void commonMerge(LabResultEntity entity, LabResultRequest request) {
        ifNotNull(request.getTestType(), entity::setTestType);
        ifNotNull(request.getResultDetails(), entity::setResultDetails);
        ifNotNull(request.getNotes(), entity::setNotes);
    }
}
