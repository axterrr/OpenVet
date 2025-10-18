package ua.edu.ukma.objectanalysis.openvet.domain.entity.examination;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class VitalSignsEntityTest {

    @Test
    void testVitalSignsEntity() {
        MedicalRecordsEntity medicalRecord = new MedicalRecordsEntity();
        medicalRecord.setId(1L);

        VitalSignsEntity vitalSigns = new VitalSignsEntity();
        vitalSigns.setId(1L);
        vitalSigns.setMedicalRecord(medicalRecord);
        vitalSigns.setRespiratoryRate(20);
        vitalSigns.setWeight(10.5);
        vitalSigns.setTemperature(38.5);
        vitalSigns.setBloodPressure(120.0);
        vitalSigns.setHeartRate(80);

        assertNotNull(vitalSigns);
        assertEquals(1L, vitalSigns.getId());
        assertEquals(medicalRecord, vitalSigns.getMedicalRecord());
        assertEquals(20, vitalSigns.getRespiratoryRate());
        assertEquals(10.5, vitalSigns.getWeight());
        assertEquals(38.5, vitalSigns.getTemperature());
        assertEquals(120.0, vitalSigns.getBloodPressure());
        assertEquals(80, vitalSigns.getHeartRate());
    }
}

