package ua.edu.ukma.objectanalysis.openvet.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.examination.MedicalRecordsEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.examination.MedicalRecordsRequest;
import ua.edu.ukma.objectanalysis.openvet.merger.BaseMerger;
import ua.edu.ukma.objectanalysis.openvet.repository.BaseRepository;
import ua.edu.ukma.objectanalysis.openvet.repository.examination.MedicalRecordsRepository;
import ua.edu.ukma.objectanalysis.openvet.validator.data.BaseValidator;
import ua.edu.ukma.objectanalysis.openvet.validator.permission.BasePermissionValidator;
import ua.edu.ukma.objectanalysis.openvet.validator.permission.MedicalRecordsPermissionValidator;

import java.util.List;

@Service
@Transactional
public class MedicalRecordsService extends BaseService<MedicalRecordsEntity, MedicalRecordsRequest, Long> {

    private final MedicalRecordsRepository medicalRecordsRepository;
    private final MedicalRecordsPermissionValidator medicalPermissionValidator;

    public MedicalRecordsService(
        BaseRepository<MedicalRecordsEntity, Long> repository,
        BaseMerger<MedicalRecordsEntity, MedicalRecordsRequest> merger,
        BaseValidator<MedicalRecordsEntity, MedicalRecordsRequest> validator,
        BasePermissionValidator<MedicalRecordsEntity, MedicalRecordsRequest> permissionValidator,
        MedicalRecordsRepository medicalRecordsRepository,
        MedicalRecordsPermissionValidator medicalPermissionValidator
    ) {
        super(repository, merger, validator, permissionValidator);
        this.medicalRecordsRepository = medicalRecordsRepository;
        this.medicalPermissionValidator = medicalPermissionValidator;
    }

    @Override
    protected MedicalRecordsEntity newEntity() {
        return new MedicalRecordsEntity();
    }

    public List<MedicalRecordsEntity> getByVeterinarian(Long veterinarianId) {
        medicalPermissionValidator.validateForGetByVeterinarian(veterinarianId);
        return medicalRecordsRepository.findAllByAppointment_TimeSlot_Veterinarian_Id(veterinarianId);
    }

    public List<MedicalRecordsEntity> getByPet(Long petId) {
        medicalPermissionValidator.validateForGetByPet(petId);
        return medicalRecordsRepository.findAllByAppointment_Pet_Id(petId);
    }

    public List<MedicalRecordsEntity> getByPetOwner(Long ownerId) {
        medicalPermissionValidator.validateForGetByPetOwner(ownerId);
        return medicalRecordsRepository.findAllByAppointment_Pet_Owner_Id(ownerId);
    }
}
