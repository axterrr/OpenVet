package ua.edu.ukma.objectanalysis.openvet.repository.pet;

import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PetEntity;
import ua.edu.ukma.objectanalysis.openvet.repository.BaseRepository;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends BaseRepository<PetEntity, Long> {
    List<PetEntity> findByOwnerId(Long ownerId);
    Optional<PetEntity> findByMicrochipNumber(String microchipNumber);

    List<PetEntity> findByOwnerIsNull();
    List<PetEntity> findByPendingOwnerPhoneNumber(String phoneNumber);

    List<PetEntity> findByOwnerPhoneNumberOrPendingOwnerPhoneNumber(String ownerPhoneNumber, String pendingOwnerPhoneNumber);
}
