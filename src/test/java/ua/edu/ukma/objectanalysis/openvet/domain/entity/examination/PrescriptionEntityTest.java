package ua.edu.ukma.objectanalysis.openvet.domain.entity.examination;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PrescriptionEntityTest {

    @Test
    void testPrescriptionEntity() {
        MedicalRecordsEntity medicalRecord = new MedicalRecordsEntity();
        medicalRecord.setId(1L);

        PrescriptionEntity prescription = new PrescriptionEntity();
        prescription.setId(1L);
        prescription.setMedicalRecord(medicalRecord);
        prescription.setMedicationName("Medication");
        prescription.setDosage("1 tablet");
        prescription.setFrequency("Once a day");
        prescription.setDuration("7 days");
        prescription.setInstructions("Take with food.");

        assertNotNull(prescription);
        assertEquals(1L, prescription.getId());
        assertEquals(medicalRecord, prescription.getMedicalRecord());
        assertEquals("Medication", prescription.getMedicationName());
        assertEquals("1 tablet", prescription.getDosage());
        assertEquals("Once a day", prescription.getFrequency());
        assertEquals("7 days", prescription.getDuration());
        assertEquals("Take with food.", prescription.getInstructions());
    }
}

