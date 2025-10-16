package ua.edu.ukma.objectanalysis.openvet.task;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.ScheduleEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.TimeSlotEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.VeterinarianEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.TimeSlotStatus;
import ua.edu.ukma.objectanalysis.openvet.repository.appointment.ScheduleRepository;
import ua.edu.ukma.objectanalysis.openvet.repository.appointment.TimeSlotRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TimeSlotGeneratorTask {

    private final ScheduleRepository scheduleRepository;
    private final TimeSlotRepository timeSlotRepository;

    private static final int DAYS_TO_GENERATE = 14;
    private static final int SLOT_DURATION_MINUTES = 30;

    @Scheduled(cron = "0 0 1 * * *")
    @Transactional
    public void generateTimeSlots() {
        LocalDate today = LocalDate.now();

        for (int i = 0; i < DAYS_TO_GENERATE; i++) {
            LocalDate targetDate = today.plusDays(i);
            DayOfWeek thatDay = targetDate.getDayOfWeek();
            List<ScheduleEntity> schedulesForThatDay = scheduleRepository.findByDayOfWeek(thatDay);

            for (ScheduleEntity schedule : schedulesForThatDay) {
                VeterinarianEntity veterinarian = schedule.getVeterinarian();

                // If the veterinarian has already booked slots for this day, skip it.
                // TODO: This is required, due to possibility of overlapping slots.
                if (timeSlotRepository.existsByVeterinarianIdAndStartTimeBetween(
                        veterinarian.getId(),
                        targetDate.atTime(schedule.getStartTime()),
                        targetDate.atTime(schedule.getEndTime())
                )) {
                    continue;
                }

                generateSlotsForSchedule(schedule, targetDate);
            }
        }
    }

    private void generateSlotsForSchedule(ScheduleEntity schedule, LocalDate date) {
        LocalTime slotTime = schedule.getStartTime();
        final LocalTime dayEndTime = schedule.getEndTime();
        final LocalTime breakStartTime = schedule.getBreakStartTime();
        final LocalTime breakEndTime = schedule.getBreakEndTime();
        final VeterinarianEntity veterinarian = schedule.getVeterinarian();

        while (slotTime.isBefore(dayEndTime)) {
            // Variable that stores the end time of the current work period (either until break or end of day)
            LocalTime currentWorkPeriodEnd = dayEndTime;

            // If the break is during the work period, adjust the end time accordingly.
            if (breakStartTime != null && !slotTime.isAfter(breakStartTime) && breakStartTime.isBefore(dayEndTime)) {
                currentWorkPeriodEnd = breakStartTime;
            }

            // If the current slot is during the break, adjust the slot time to the break end time.
            if (breakStartTime != null && !slotTime.isBefore(breakStartTime) && slotTime.isBefore(breakEndTime)) {
                slotTime = breakEndTime;
                continue;
            }

            // Calculate the start and potential end of the next standard slot
            LocalTime nextSlotStartTime = slotTime.plusMinutes(SLOT_DURATION_MINUTES);
            LocalDateTime slotStartDateTime = LocalDateTime.of(date, slotTime);
            LocalDateTime slotEndDateTime;

            // If the next standard slot would start after the work period ends, this is the last slot.
            // Adjust its end time to match the period's end time exactly.
            if (nextSlotStartTime.isAfter(currentWorkPeriodEnd)) {
                slotEndDateTime = LocalDateTime.of(date, currentWorkPeriodEnd);
            } else {
                // It's a standard, full-duration slot
                slotEndDateTime = LocalDateTime.of(date, nextSlotStartTime);
            }

            // Prevent creating duplicate slots
            if (!timeSlotRepository.existsByVeterinarianIdAndStartTime(veterinarian.getId(), slotStartDateTime)) {
                TimeSlotEntity timeSlot = TimeSlotEntity.builder()
                        .veterinarian(veterinarian)
                        .startTime(slotStartDateTime)
                        .endTime(slotEndDateTime)
                        .status(TimeSlotStatus.AVAILABLE)
                        .build();
                timeSlotRepository.save(timeSlot);
            }

            // If the slot we just created ends exactly where a break begins,
            // the next slot must start after the break ends.
            if (slotEndDateTime.toLocalTime().equals(breakStartTime)) {
                slotTime = breakEndTime;
            } else {
                slotTime = nextSlotStartTime;
            }
        }
    }
}
