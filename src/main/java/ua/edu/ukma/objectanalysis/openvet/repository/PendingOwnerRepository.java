package ua.edu.ukma.objectanalysis.openvet.repository;

import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PendingOwnerEntity;

public interface PendingOwnerRepository extends BaseRepository<PendingOwnerEntity, Long> {
    PendingOwnerEntity findByPhoneNumber(String phoneNumber);
}
