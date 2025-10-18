package ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment;

import org.junit.jupiter.api.Test;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PetEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.AppointmentStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AppointmentEntityTest {

    @Test
    void testAppointmentEntity() {
        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setId(1L);
        appointment.setReasonForVisit("Regular checkup");
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setNotes("No specific notes");

        PetEntity pet = new PetEntity();
        pet.setId(1L);
        appointment.setPet(pet);

        TimeSlotEntity timeSlot = new TimeSlotEntity();
        timeSlot.setId(1L);
        appointment.setTimeSlot(timeSlot);

        assertEquals(1L, appointment.getId());
        assertEquals("Regular checkup", appointment.getReasonForVisit());
        assertEquals(AppointmentStatus.SCHEDULED, appointment.getStatus());
        assertEquals("No specific notes", appointment.getNotes());
        assertEquals(pet, appointment.getPet());
        assertEquals(timeSlot, appointment.getTimeSlot());
    }

    @Test
    void testAppointmentEntityBuilder() {
        PetEntity pet = new PetEntity();
        pet.setId(1L);

        TimeSlotEntity timeSlot = new TimeSlotEntity();
        timeSlot.setId(1L);

        AppointmentEntity appointment = AppointmentEntity.builder()
                .id(1L)
                .reasonForVisit("Regular checkup")
                .status(AppointmentStatus.SCHEDULED)
                .notes("No specific notes")
                .pet(pet)
                .timeSlot(timeSlot)
                .build();

        assertEquals(1L, appointment.getId());
        assertEquals("Regular checkup", appointment.getReasonForVisit());
        assertEquals(AppointmentStatus.SCHEDULED, appointment.getStatus());
        assertEquals("No specific notes", appointment.getNotes());
        assertEquals(pet, appointment.getPet());
        assertEquals(timeSlot, appointment.getTimeSlot());
    }

    @Test
    void testAllArgsConstructor() {
        PetEntity pet = new PetEntity();
        pet.setId(1L);

        TimeSlotEntity timeSlot = new TimeSlotEntity();
        timeSlot.setId(1L);

        AppointmentEntity appointment = new AppointmentEntity(1L, pet, timeSlot, "Regular checkup",
                AppointmentStatus.SCHEDULED, "No specific notes", null, null);

        assertEquals(1L, appointment.getId());
        assertEquals("Regular checkup", appointment.getReasonForVisit());
        assertEquals(AppointmentStatus.SCHEDULED, appointment.getStatus());
        assertEquals("No specific notes", appointment.getNotes());
        assertEquals(pet, appointment.getPet());
        assertEquals(timeSlot, appointment.getTimeSlot());
    }

    @Test
    void testNoArgsConstructor() {
        AppointmentEntity appointment = new AppointmentEntity();
        assertNotNull(appointment);
    }
}

