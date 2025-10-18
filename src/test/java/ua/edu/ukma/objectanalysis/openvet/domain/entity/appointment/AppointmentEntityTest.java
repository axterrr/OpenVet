package ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment;

import org.junit.jupiter.api.Test;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PetEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.AppointmentStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AppointmentEntityTest {

    @Test
    void testAppointmentEntity() {
        PetEntity pet = new PetEntity();
        pet.setId(1L);

        TimeSlotEntity timeSlot = new TimeSlotEntity();
        timeSlot.setId(1L);

        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setId(1L);
        appointment.setPet(pet);
        appointment.setTimeSlot(timeSlot);
        appointment.setReasonForVisit("Regular checkup");
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setNotes("No specific notes.");

        assertNotNull(appointment);
        assertEquals(1L, appointment.getId());
        assertEquals(pet, appointment.getPet());
        assertEquals(timeSlot, appointment.getTimeSlot());
        assertEquals("Regular checkup", appointment.getReasonForVisit());
        assertEquals(AppointmentStatus.SCHEDULED, appointment.getStatus());
        assertEquals("No specific notes.", appointment.getNotes());
    }
}

