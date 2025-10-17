package ua.edu.ukma.objectanalysis.openvet.merger;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PendingOwnerEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PetEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.PetOwnerEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.pet.PetRequest;
import ua.edu.ukma.objectanalysis.openvet.repository.pet.PendingOwnerRepository;
import ua.edu.ukma.objectanalysis.openvet.repository.user.PetOwnerRepository;

import java.util.Optional;

import static ua.edu.ukma.objectanalysis.openvet.merger.MergerUtils.ifNotNull;

@Component
@RequiredArgsConstructor
public class PetMerger implements BaseMerger<PetEntity, PetRequest> {

    private final PetOwnerRepository petOwnerRepository;
    private final PendingOwnerRepository pendingOwnerRepository;

    @Override
    public void mergeCreate(PetEntity entity, PetRequest request) {
        if (request == null || entity == null) { return; }
        commonMerge(entity, request);

        if (request.getOwnerPhone() != null) {
            Optional<PetOwnerEntity> owner = petOwnerRepository.findByPhoneNumber(request.getOwnerPhone());
            if (owner.isPresent()) {
                entity.setOwner(owner.get());
            } else {
                PendingOwnerEntity pending = pendingOwnerRepository.findByPhoneNumber(request.getOwnerPhone())
                    .orElse(pendingOwnerRepository.saveAndFlush(
                        PendingOwnerEntity.builder().phoneNumber(request.getOwnerPhone()).build()
                    ));
                entity.setPendingOwner(pending);
            }
        }
    }

    @Override
    public void mergeUpdate(PetEntity entity, PetRequest request) {
        if (request == null || entity == null) { return; }
        commonMerge(entity, request);
    }

    private void commonMerge(PetEntity entity, PetRequest request) {
        ifNotNull(request.getName(), entity::setName);
        ifNotNull(request.getSpecies(), entity::setSpecies);
        ifNotNull(request.getBreed(), entity::setBreed);
        ifNotNull(request.getBirthDate(), entity::setBirthDate);
        ifNotNull(request.getSex(), entity::setSex);
        ifNotNull(request.getColor(), entity::setColor);
        ifNotNull(request.getNeutered(), entity::setIsNeutered);
        ifNotNull(request.getMicrochipNumber(), entity::setMicrochipNumber);
    }
}

