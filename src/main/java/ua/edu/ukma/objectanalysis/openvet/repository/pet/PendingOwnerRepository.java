package ua.edu.ukma.objectanalysis.openvet.repository.pet;

import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PendingOwnerEntity;
import ua.edu.ukma.objectanalysis.openvet.repository.BaseRepository;

public interface PendingOwnerRepository extends BaseRepository<PendingOwnerEntity, Long> {
    PendingOwnerEntity findByPhoneNumber(String phoneNumber);
}
