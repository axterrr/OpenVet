package ua.edu.ukma.objectanalysis.openvet.task;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.AppointmentEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.PetOwnerEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.AppointmentStatus;
import ua.edu.ukma.objectanalysis.openvet.repository.appointment.AppointmentRepository;
import ua.edu.ukma.objectanalysis.openvet.service.EmailService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EmailSenderTask {

    private final EmailService emailService;
    private final AppointmentRepository appointmentRepository;

    private final String appointmentReminderSubject = "OpenVet - Appointment Reminder";
    private final String appointmentReminderBody = """
        Dear %s,
    
        This is a reminder from OpenVet that you have an appointment scheduled for tomorrow at %s for your pet %s.
    
        Please arrive on time or contact us if you need to reschedule.
    
        Thank you,
        The OpenVet Team
        """;

    @Scheduled(cron = "0 0 12 * * *")
    @Transactional
    public void sendAppointmentReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDateTime start = tomorrow.atTime(LocalTime.MIN);
        LocalDateTime end = tomorrow.atTime(LocalTime.MAX);

        List<AppointmentEntity> tomorrowAppointments = appointmentRepository.findByDate(start, end, AppointmentStatus.SCHEDULED);

        tomorrowAppointments.forEach(appointment -> {
            PetOwnerEntity owner = appointment.getPet().getOwner();
            String time = appointment.getTimeSlot().getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            String petName = appointment.getPet().getName();
            emailService.sendEmail(
                owner.getEmail(), appointmentReminderSubject,
                appointmentReminderBody.formatted(owner.getFirstName(), time, petName)
            );
        });
    }
}
