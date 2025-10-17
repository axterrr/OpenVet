package ua.edu.ukma.objectanalysis.openvet.repository.pet;

import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PendingOwnerEntity;
import ua.edu.ukma.objectanalysis.openvet.repository.BaseRepository;

import java.util.Optional;

public interface PendingOwnerRepository extends BaseRepository<PendingOwnerEntity, Long> {
    Optional<PendingOwnerEntity> findByPhoneNumber(String phoneNumber);
}
