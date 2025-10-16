package ua.edu.ukma.objectanalysis.openvet.repository.pet;

import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PetEntity;
import ua.edu.ukma.objectanalysis.openvet.repository.BaseRepository;

import java.util.List;

public interface PetRepository extends BaseRepository<PetEntity, Long> {
    List<PetEntity> findByOwnerId(Long ownerId);
    List<PetEntity> findByMicrochipNumber(String microchipNumber);

    List<PetEntity> findByOwnerIsNull();
    List<PetEntity> findByPendingOwnerPhoneNumber(String phoneNumber);

    List<PetEntity> findByOwnerPhoneNumberOrPendingOwnerPhoneNumber(String ownerPhoneNumber, String pendingOwnerPhoneNumber);
}
