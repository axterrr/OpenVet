package ua.edu.ukma.objectanalysis.openvet.validator.data;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.UserEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.user.UserRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.ConflictException;
import ua.edu.ukma.objectanalysis.openvet.exception.ValidationException;
import ua.edu.ukma.objectanalysis.openvet.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserValidator extends BaseValidator<UserEntity, UserRequest> {

    private final UserRepository userRepository;

    @Override
    public void validateForCreate(UserRequest request) {
        super.validateForCreate(request);

        validatePassword(request.getPassword());

        if (userRepository.existsByPhoneNumber(request.getPhone())) {
            throw new ConflictException("User with such phone already exists");
        }
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("User with such email already exists");
        }
    }

    @Override
    public void validateForUpdate(UserRequest request, UserEntity entity) {
        super.validateForUpdate(request, entity);

        if (request.getPassword() != null) {
            validatePassword(request.getPassword());
        }

        if (request.getPhone() != null) {
            userRepository.findByPhoneNumber(request.getPhone()).ifPresent(existing -> {
                if (!existing.getId().equals(entity.getId())) {
                    throw new ConflictException("User with such phone already exists");
                }
            });
        }
        if (request.getEmail() != null) {
            userRepository.findByEmail(request.getEmail()).ifPresent(existing -> {
                if (!existing.getId().equals(entity.getId())) {
                    throw new ConflictException("User with such email already exists");
                }
            });
        }
    }

    private void validatePassword(String password) {
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
        }

        if (!hasUpper || !hasLower || !hasDigit) {
            throw new ValidationException("Password must contain upper and lower letters and digits");
        }
    }
}
