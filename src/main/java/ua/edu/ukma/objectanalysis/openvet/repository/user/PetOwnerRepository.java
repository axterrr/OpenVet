package ua.edu.ukma.objectanalysis.openvet.repository.user;

import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.PetOwnerEntity;
import ua.edu.ukma.objectanalysis.openvet.repository.BaseRepository;

import java.util.Optional;

public interface PetOwnerRepository extends BaseRepository<PetOwnerEntity, Long> {
    Optional<PetOwnerEntity> findByPhoneNumber(String phone);
}
