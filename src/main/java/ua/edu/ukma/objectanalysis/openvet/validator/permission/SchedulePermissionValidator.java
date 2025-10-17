package ua.edu.ukma.objectanalysis.openvet.validator.permission;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.ScheduleEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.UserEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.appointment.ScheduleRequest;
import ua.edu.ukma.objectanalysis.openvet.repository.user.UserRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SchedulePermissionValidator extends BasePermissionValidator<ScheduleEntity, ScheduleRequest> {

    private final UserRepository userRepository;

    @Override
    public void validateForGetAll() {
        require(isAuthenticatedUserAdmin());
    }

    // Should be visible for everyone?
    @Override
    public void validateForGet(ScheduleEntity entity) {}

    public void validateForMultiple(List<ScheduleEntity> entities) {}

    @Override
    public void validateForCreate(ScheduleRequest request) {
        if (isAuthenticatedUserAdmin()) { return; }
        if (isAuthenticatedUserVeterinarian()) {
            require(request.getVeterinarianId() != null);
            UserEntity me = getAuthenticatedUser();
            require(me != null && me.getId().equals(request.getVeterinarianId()));
            return;
        }
        forbid();
    }

    @Override
    public void validateForUpdate(ScheduleEntity entity) {
        if (isAuthenticatedUserAdmin()) { return; }
        if (isAuthenticatedUserVeterinarian()) {
            require(entity.getVeterinarian() != null);
            require(entity.getVeterinarian().getEmail().equals(getAuthenticatedUserEmail()));
            return;
        }
        forbid();
    }

    @Override
    public void validateForDelete(ScheduleEntity entity) {
        validateForUpdate(entity);
    }

    private UserEntity getAuthenticatedUser() {
        String email = getAuthenticatedUserEmail();
        if (email == null) { return null; }
        return userRepository.findByEmail(email).orElse(null);
    }
}

