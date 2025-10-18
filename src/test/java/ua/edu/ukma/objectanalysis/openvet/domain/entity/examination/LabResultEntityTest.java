package ua.edu.ukma.objectanalysis.openvet.domain.entity.examination;

import org.junit.jupiter.api.Test;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.LabTestType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LabResultEntityTest {

    @Test
    void testLabResultEntity() {
        MedicalRecordsEntity medicalRecord = new MedicalRecordsEntity();
        medicalRecord.setId(1L);

        LabResultEntity labResult = new LabResultEntity();
        labResult.setId(1L);
        labResult.setMedicalRecord(medicalRecord);
        labResult.setTestType(LabTestType.BLOOD_TEST);
        labResult.setResultDetails("All normal");
        labResult.setNotes("No specific notes.");

        assertNotNull(labResult);
        assertEquals(1L, labResult.getId());
        assertEquals(medicalRecord, labResult.getMedicalRecord());
        assertEquals(LabTestType.BLOOD_TEST, labResult.getTestType());
        assertEquals("All normal", labResult.getResultDetails());
        assertEquals("No specific notes.", labResult.getNotes());
    }
}

