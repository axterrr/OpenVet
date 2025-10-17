package ua.edu.ukma.objectanalysis.openvet.validator.data;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.examination.MedicalRecordsEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.examination.MedicalRecordsRequest;

@Component
@RequiredArgsConstructor
public class MedicalRecordsValidator extends BaseValidator<MedicalRecordsEntity, MedicalRecordsRequest> {

}
