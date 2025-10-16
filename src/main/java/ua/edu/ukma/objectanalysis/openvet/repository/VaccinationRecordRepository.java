package ua.edu.ukma.objectanalysis.openvet.repository;

import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.VaccinationRecordEntity;

import java.time.LocalDate;
import java.util.List;

public interface VaccinationRecordRepository extends BaseRepository<VaccinationRecordEntity, Long>{
    List<VaccinationRecordEntity> findByVeterinarianId(Long veterinarianId);
    List<VaccinationRecordEntity> findByPetId(Long petId);
    List<VaccinationRecordEntity> findByPetMicrochipNumber(String petMicrochipNumber);

    List<VaccinationRecordEntity> findByNextDueDateAfter(LocalDate nextDueDateAfter);
}
