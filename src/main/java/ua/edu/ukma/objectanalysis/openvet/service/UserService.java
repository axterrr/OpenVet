package ua.edu.ukma.objectanalysis.openvet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.AdminEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.PetOwnerEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.UserEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.VeterinarianEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.user.UserRequest;

@Service
@Transactional
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
            case PET_OWNER -> new PetOwnerEntity();
            case VETERINARIAN -> new VeterinarianEntity();
            case ADMIN -> new AdminEntity();
        };

        merger.mergeCreate(entity, request);
        return repository.saveAndFlush(entity);
    }
}
