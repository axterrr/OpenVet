package ua.edu.ukma.objectanalysis.openvet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.Admin;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.PetOwner;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.UserEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.Veterinarian;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;
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
        // reuse the same validation and permission checks from BaseService.create
        permissionValidator.validateForCreate(request);
        validator.validateForCreate(request);

        // create the correct concrete subclass so JPA writes the proper discriminator value
        UserEntity entity;
        UserRole role = request.getRole();
        if (role == null) {
            entity = new UserEntity();
        } else {
            switch (role) {
                case PET_OWNER:
                    entity = new PetOwner();
                    break;
                case VETERINARIAN:
                    entity = new Veterinarian();
                    break;
                case ADMIN:
                    entity = new Admin();
                    break;
                default:
                    entity = new UserEntity();
            }
        }

        merger.merge(entity, request);
        return repository.saveAndFlush(entity);
    }
}
