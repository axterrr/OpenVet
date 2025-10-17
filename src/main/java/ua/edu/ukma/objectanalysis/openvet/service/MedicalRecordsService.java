package ua.edu.ukma.objectanalysis.openvet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.examination.MedicalRecordsEntity;

@Service
@Transactional
@RequiredArgsConstructor
public class MedicalRecordsService extends BaseService<MedicalRecordsEntity, MedicalRecordsEntity, Long> {

    @Override
    protected MedicalRecordsEntity newEntity() {
        return new MedicalRecordsEntity();
    }
}
