package ua.edu.ukma.objectanalysis.openvet.domain.entity.examination;

import org.junit.jupiter.api.Test;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.AppointmentEntity;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MedicalRecordsEntityTest {

    @Test
    void testMedicalRecordsEntity() {
        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setId(1L);

        MedicalRecordsEntity medicalRecord = new MedicalRecordsEntity();
        medicalRecord.setId(1L);
        medicalRecord.setAppointment(appointment);
        medicalRecord.setDiagnosis("Allergy");
        medicalRecord.setTreatment("Antihistamines");
        medicalRecord.setNotes("Avoid allergens.");
        medicalRecord.setPrescriptions(new HashSet<>());
        medicalRecord.setLabResults(new HashSet<>());

        assertNotNull(medicalRecord);
        assertEquals(1L, medicalRecord.getId());
        assertEquals(appointment, medicalRecord.getAppointment());
        assertEquals("Allergy", medicalRecord.getDiagnosis());
        assertEquals("Antihistamines", medicalRecord.getTreatment());
        assertEquals("Avoid allergens.", medicalRecord.getNotes());
        assertNotNull(medicalRecord.getPrescriptions());
        assertNotNull(medicalRecord.getLabResults());
    }
}

