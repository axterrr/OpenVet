package ua.edu.ukma.objectanalysis.openvet.merger;

import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PetEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.pet.PetRequest;

import static ua.edu.ukma.objectanalysis.openvet.merger.MergerUtils.ifNotNull;

@Component
public class PetMerger implements BaseMerger<PetEntity, PetRequest> {

    @Override
    public void mergeCreate(PetEntity entity, PetRequest request) {
        if (request == null || entity == null) { return; }
        common(entity, request);
    }

    @Override
    public void mergeUpdate(PetEntity entity, PetRequest request) {
        if (request == null || entity == null) { return; }
        common(entity, request);
    }

    private void common(PetEntity entity, PetRequest request) {
        ifNotNull(request.getName(), entity::setName);
        ifNotNull(request.getSpecies(), entity::setSpecies);
        ifNotNull(request.getBreed(), entity::setBreed);
        ifNotNull(request.getBirthDate(), entity::setBirthDate);
        ifNotNull(request.getSex(), entity::setSex);
        ifNotNull(request.getColor(), entity::setColor);
        ifNotNull(request.getNeutered(), entity::setNeutered);
        ifNotNull(request.getMicrochipNumber(), entity::setMicrochipNumber);
        // Owner is not updated by this method !!!
        // It is handled by a PetService
    }
}

