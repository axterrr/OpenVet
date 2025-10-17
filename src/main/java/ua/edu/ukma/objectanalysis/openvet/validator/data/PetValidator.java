package ua.edu.ukma.objectanalysis.openvet.validator.data;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PetEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.pet.PetRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.ConflictException;
import ua.edu.ukma.objectanalysis.openvet.repository.pet.PetRepository;

@Component
@RequiredArgsConstructor
public class PetValidator extends BaseValidator<PetEntity, PetRequest> {

    private final PetRepository petRepository;

    @Override
    public void validateForCreate(PetRequest request) {
        super.validateForCreate(request);
        if (request.getMicrochipNumber() != null) {
            boolean exists = !petRepository.findByMicrochipNumber(request.getMicrochipNumber()).isEmpty();
            if (exists) {
                throw new ConflictException("Pet with such microchip number already exists");
            }
        }
    }

    @Override
    public void validateForUpdate(PetRequest request, PetEntity entity) {
        super.validateForUpdate(request, entity);
        if (request.getMicrochipNumber() != null) {
            petRepository.findByMicrochipNumber(request.getMicrochipNumber()).stream()
                .filter(other -> !other.getId().equals(entity.getId()))
                .findFirst()
                .ifPresent(other -> { throw new ConflictException("Pet with such microchip number already exists"); });
        }
    }
}

