package ua.edu.ukma.objectanalysis.openvet.task;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.AppointmentEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.TimeSlotEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PetEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.PetOwnerEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.AppointmentStatus;
import ua.edu.ukma.objectanalysis.openvet.repository.appointment.AppointmentRepository;
import ua.edu.ukma.objectanalysis.openvet.service.EmailService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailSenderTaskTest {

    @Mock
    private EmailService emailService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private EmailSenderTask emailSenderTask;

    @Test
    void sendAppointmentReminders_shouldSendEmails_whenAppointmentsExist() {
        PetOwnerEntity owner = new PetOwnerEntity();
        owner.setFirstName("Ivan");
        owner.setEmail("ivan.test@example.com");

        PetEntity pet = new PetEntity();
        pet.setName("Rex");
        pet.setOwner(owner);

        TimeSlotEntity timeSlot = new TimeSlotEntity();
        timeSlot.setStartTime(LocalDateTime.of(2022, 1, 1, 15, 0));

        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setPet(pet);
        appointment.setTimeSlot(timeSlot);

        when(appointmentRepository.findByDate(any(LocalDateTime.class), any(LocalDateTime.class), eq(AppointmentStatus.SCHEDULED)))
            .thenReturn(List.of(appointment));

        emailSenderTask.sendAppointmentReminders();

        ArgumentCaptor<String> toCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);

        verify(emailService, only()).sendEmail(toCaptor.capture(), subjectCaptor.capture(), bodyCaptor.capture());

        String expectedSubject = "OpenVet - Appointment Reminder";
        String expectedBody = """
            Dear Ivan,
            
            This is a reminder from OpenVet that you have an appointment scheduled for tomorrow at 15:00 for your pet Rex.
            
            Please arrive on time or contact us if you need to reschedule.
            
            Thank you,
            The OpenVet Team
            """;

        assertEquals("ivan.test@example.com", toCaptor.getValue());
        assertEquals(expectedSubject, subjectCaptor.getValue());
        assertEquals(expectedBody, bodyCaptor.getValue());
    }

    @Test
    void sendAppointmentReminders_shouldNotSendEmails_whenNoAppointmentsExist() {
        when(appointmentRepository.findByDate(any(LocalDateTime.class), any(LocalDateTime.class), eq(AppointmentStatus.SCHEDULED)))
            .thenReturn(Collections.emptyList());

        emailSenderTask.sendAppointmentReminders();

        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
    }
}
