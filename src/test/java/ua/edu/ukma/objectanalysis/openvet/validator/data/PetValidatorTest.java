package ua.edu.ukma.objectanalysis.openvet.validator.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PetEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.pet.PetRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.ConflictException;
import ua.edu.ukma.objectanalysis.openvet.repository.pet.PetRepository;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetValidatorTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private jakarta.validation.Validator validator;

    @InjectMocks
    private PetValidator petValidator;

    private PetRequest petRequest;
    private PetEntity petEntity;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        petRequest = new PetRequest();
        petRequest.setMicrochipNumber("12345");

        petEntity = new PetEntity();
        petEntity.setId(1L);
        petEntity.setMicrochipNumber("12345");

        when(validator.validate(any())).thenReturn(Collections.emptySet());
        Field validatorField = BaseValidator.class.getDeclaredField("validator");
        validatorField.setAccessible(true);
        validatorField.set(petValidator, validator);
    }

    @Test
    void validateForCreate_UniqueMicrochip_NoException() {
        when(petRepository.findByMicrochipNumber("12345")).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> petValidator.validateForCreate(petRequest));
    }

    @Test
    void validateForCreate_DuplicateMicrochip_ThrowsConflictException() {
        when(petRepository.findByMicrochipNumber("12345")).thenReturn(Optional.of(new PetEntity()));
        assertThrows(ConflictException.class, () -> petValidator.validateForCreate(petRequest));
    }

    @Test
    void validateForCreate_NullMicrochip_NoException() {
        petRequest.setMicrochipNumber(null);
        assertDoesNotThrow(() -> petValidator.validateForCreate(petRequest));
    }

    @Test
    void validateForUpdate_UniqueMicrochip_NoException() {
        when(petRepository.findByMicrochipNumber("12345")).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> petValidator.validateForUpdate(petRequest, petEntity));
    }

    @Test
    void validateForUpdate_DuplicateMicrochip_SamePet_NoException() {
        when(petRepository.findByMicrochipNumber("12345")).thenReturn(Optional.of(petEntity));
        assertDoesNotThrow(() -> petValidator.validateForUpdate(petRequest, petEntity));
    }

    @Test
    void validateForUpdate_DuplicateMicrochip_OtherPet_ThrowsConflictException() {
        PetEntity otherPet = new PetEntity();
        otherPet.setId(2L);
        when(petRepository.findByMicrochipNumber("12345")).thenReturn(Optional.of(otherPet));
        assertThrows(ConflictException.class, () -> petValidator.validateForUpdate(petRequest, petEntity));
    }

    @Test
    void validateForUpdate_NullMicrochip_NoException() {
        petRequest.setMicrochipNumber(null);
        assertDoesNotThrow(() -> petValidator.validateForUpdate(petRequest, petEntity));
    }
}
