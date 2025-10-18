package ua.edu.ukma.objectanalysis.openvet.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.AdminEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.PetOwnerEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.UserEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.VeterinarianEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;
import ua.edu.ukma.objectanalysis.openvet.dto.user.UserRequest;
import ua.edu.ukma.objectanalysis.openvet.merger.BaseMerger;
import ua.edu.ukma.objectanalysis.openvet.repository.BaseRepository;
import ua.edu.ukma.objectanalysis.openvet.repository.user.UserRepository;
import ua.edu.ukma.objectanalysis.openvet.validator.data.BaseValidator;
import ua.edu.ukma.objectanalysis.openvet.validator.permission.BasePermissionValidator;
import ua.edu.ukma.objectanalysis.openvet.validator.permission.UserPermissionValidator;

import java.util.List;

@Service
@Transactional
public class UserService extends BaseService<UserEntity, UserRequest, Long> {

    private final UserRepository userRepository;
    private final UserPermissionValidator userPermissionValidator;

    public UserService(
        BaseRepository<UserEntity, Long> repository,
        BaseMerger<UserEntity, UserRequest> merger,
        BaseValidator<UserEntity, UserRequest> validator,
        BasePermissionValidator<UserEntity, UserRequest> permissionValidator,
        UserRepository userRepository,
        UserPermissionValidator userPermissionValidator
    ) {
        super(repository, merger, validator, permissionValidator);
        this.userRepository = userRepository;
        this.userPermissionValidator = userPermissionValidator;
    }

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

    public List<UserEntity> getByRole(UserRole role) {
        userPermissionValidator.validateForGetByRole(role);
        return userRepository.findAllByRole(role);
    }
}
