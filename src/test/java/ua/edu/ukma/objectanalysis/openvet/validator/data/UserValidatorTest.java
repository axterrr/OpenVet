package ua.edu.ukma.objectanalysis.openvet.validator.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.UserEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.user.UserRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.ConflictException;
import ua.edu.ukma.objectanalysis.openvet.exception.ValidationException;
import ua.edu.ukma.objectanalysis.openvet.repository.user.UserRepository;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private jakarta.validation.Validator validator;

    @InjectMocks
    private UserValidator userValidator;

    private UserRequest userRequest;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        userRequest = new UserRequest();
        userRequest.setPhone("1234567890");
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("Password123");

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setPhoneNumber("1234567890");
        userEntity.setEmail("test@example.com");

        when(validator.validate(any())).thenReturn(Collections.emptySet());
        Field validatorField = BaseValidator.class.getDeclaredField("validator");
        validatorField.setAccessible(true);
        validatorField.set(userValidator, validator);
    }

    @Test
    void validateForCreate_ValidRequest_NoException() {
        when(userRepository.existsByPhoneNumber("1234567890")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        assertDoesNotThrow(() -> userValidator.validateForCreate(userRequest));
    }

    @Test
    void validateForCreate_ExistingPhone_ThrowsConflictException() {
        when(userRepository.existsByPhoneNumber("1234567890")).thenReturn(true);
        assertThrows(ConflictException.class, () -> userValidator.validateForCreate(userRequest));
    }

    @Test
    void validateForCreate_ExistingEmail_ThrowsConflictException() {
        when(userRepository.existsByPhoneNumber("1234567890")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
        assertThrows(ConflictException.class, () -> userValidator.validateForCreate(userRequest));
    }

    @Test
    void validateForCreate_InvalidPassword_ThrowsValidationException() {
        userRequest.setPassword("password");
        assertThrows(ValidationException.class, () -> userValidator.validateForCreate(userRequest));
    }

    @Test
    void validateForUpdate_ValidRequest_NoException() {
        assertDoesNotThrow(() -> userValidator.validateForUpdate(userRequest, userEntity));
    }

    @Test
    void validateForUpdate_ExistingPhoneOnOtherUser_ThrowsConflictException() {
        UserEntity otherUser = new UserEntity();
        otherUser.setId(2L);
        when(userRepository.findByPhoneNumber("1234567890")).thenReturn(Optional.of(otherUser));
        assertThrows(ConflictException.class, () -> userValidator.validateForUpdate(userRequest, userEntity));
    }

    @Test
    void validateForUpdate_ExistingEmailOnOtherUser_ThrowsConflictException() {
        UserEntity otherUser = new UserEntity();
        otherUser.setId(2L);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(otherUser));
        assertThrows(ConflictException.class, () -> userValidator.validateForUpdate(userRequest, userEntity));
    }

    @Test
    void validateForUpdate_InvalidPassword_ThrowsValidationException() {
        userRequest.setPassword("password");
        assertThrows(ValidationException.class, () -> userValidator.validateForUpdate(userRequest, userEntity));
    }
}
