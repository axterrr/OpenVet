package ua.edu.ukma.objectanalysis.openvet.merger.examination;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.examination.VitalSignsEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.examination.VitalSignsRequest;
import ua.edu.ukma.objectanalysis.openvet.merger.BaseMerger;

import static ua.edu.ukma.objectanalysis.openvet.merger.MergerUtils.ifNotNull;

@Component
@RequiredArgsConstructor
public class VitalSignsMerger implements BaseMerger<VitalSignsEntity, VitalSignsRequest> {

    @Override
    public void mergeCreate(VitalSignsEntity entity, VitalSignsRequest request) {
        if (request == null || entity == null) { return; }
        commonMerge(entity, request);
    }

    @Override
    public void mergeUpdate(VitalSignsEntity entity, VitalSignsRequest request) {
        if (request == null || entity == null) { return; }
        commonMerge(entity, request);
    }

    private void commonMerge(VitalSignsEntity entity, VitalSignsRequest request) {
        ifNotNull(request.getRespiratoryRate(), entity::setRespiratoryRate);
        ifNotNull(request.getWeight(), entity::setWeight);
        ifNotNull(request.getTemperature(), entity::setTemperature);
        ifNotNull(request.getBloodPressure(), entity::setBloodPressure);
        ifNotNull(request.getHeartRate(), entity::setHeartRate);
    }
}
