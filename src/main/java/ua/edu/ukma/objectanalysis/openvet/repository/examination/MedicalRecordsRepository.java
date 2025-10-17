package ua.edu.ukma.objectanalysis.openvet.repository.examination;

import ua.edu.ukma.objectanalysis.openvet.domain.entity.examination.MedicalRecordsEntity;
import ua.edu.ukma.objectanalysis.openvet.repository.BaseRepository;

import java.util.List;

public interface MedicalRecordsRepository extends BaseRepository<MedicalRecordsEntity, Long> {
    List<MedicalRecordsEntity> findAllByAppointment_TimeSlot_Veterinarian_Id(Long veterinarianId);
    List<MedicalRecordsEntity> findAllByAppointment_Pet_Id(Long petId);
    List<MedicalRecordsEntity> findAllByAppointment_Pet_Owner_Id(Long ownerId);
}
