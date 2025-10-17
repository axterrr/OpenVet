package ua.edu.ukma.objectanalysis.openvet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.examination.MedicalRecordsEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.examination.MedicalRecordsRequest;

@Service
@Transactional
@RequiredArgsConstructor
public class MedicalRecordsService extends BaseService<MedicalRecordsEntity, MedicalRecordsRequest, Long> {

    @Override
    protected MedicalRecordsEntity newEntity() {
        return new MedicalRecordsEntity();
    }
}
