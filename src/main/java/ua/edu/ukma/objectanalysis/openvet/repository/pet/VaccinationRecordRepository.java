package ua.edu.ukma.objectanalysis.openvet.repository.pet;

import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.VaccinationRecordEntity;
import ua.edu.ukma.objectanalysis.openvet.repository.BaseRepository;

import java.time.LocalDate;
import java.util.List;

public interface VaccinationRecordRepository extends BaseRepository<VaccinationRecordEntity, Long> {
    List<VaccinationRecordEntity> findByVeterinarianId(Long veterinarianId);
    List<VaccinationRecordEntity> findByPetId(Long petId);
    List<VaccinationRecordEntity> findByPetMicrochipNumber(String petMicrochipNumber);

    List<VaccinationRecordEntity> findByNextDueDateAfter(LocalDate nextDueDateAfter);
}
