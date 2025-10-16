package ua.edu.ukma.objectanalysis.openvet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.Admin;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.PetOwner;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.UserEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.Veterinarian;
import ua.edu.ukma.objectanalysis.openvet.dto.user.UserRequest;

@Service
@RequiredArgsConstructor
public class UserService extends BaseService<UserEntity, UserRequest, Long> {

    @Override
    protected UserEntity newEntity() {
        return new UserEntity();
    }

    @Override
    public UserEntity create(UserRequest request) {
        permissionValidator.validateForCreate(request);
        validator.validateForCreate(request);

        UserEntity entity = switch (request.getRole()) {
            case PET_OWNER -> new PetOwner();
            case VETERINARIAN -> new Veterinarian();
            case ADMIN -> new Admin();
        };

        merger.merge(entity, request);
        return repository.saveAndFlush(entity);
    }
}
