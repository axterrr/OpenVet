package ua.edu.ukma.objectanalysis.openvet.validator.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.examination.MedicalRecordsEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.examination.MedicalRecordsRequest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedicalRecordsValidatorTest {

    @Mock
    private jakarta.validation.Validator validator;

    @InjectMocks
    private MedicalRecordsValidator medicalRecordsValidator;

    @Test
    void testValidateForCreate() {
        when(validator.validate(any())).thenReturn(Collections.emptySet());
        assertDoesNotThrow(() -> medicalRecordsValidator.validateForCreate(new MedicalRecordsRequest()));
    }

    @Test
    void testValidateForUpdate() {
        when(validator.validate(any())).thenReturn(Collections.emptySet());
        assertDoesNotThrow(() -> medicalRecordsValidator.validateForUpdate(new MedicalRecordsRequest(), new MedicalRecordsEntity()));
    }
}
