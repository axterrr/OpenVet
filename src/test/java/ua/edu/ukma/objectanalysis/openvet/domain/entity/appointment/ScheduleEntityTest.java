package ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment;

import org.junit.jupiter.api.Test;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.VeterinarianEntity;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ScheduleEntityTest {

    @Test
    void testScheduleEntity() {
        VeterinarianEntity veterinarian = new VeterinarianEntity();
        veterinarian.setId(1L);

        ScheduleEntity schedule = new ScheduleEntity();
        schedule.setId(1L);
        schedule.setVeterinarian(veterinarian);
        schedule.setDayOfWeek(DayOfWeek.MONDAY);
        schedule.setStartTime(LocalTime.of(9, 0));
        schedule.setEndTime(LocalTime.of(17, 0));
        schedule.setBreakStartTime(LocalTime.of(12, 0));
        schedule.setBreakEndTime(LocalTime.of(13, 0));

        assertNotNull(schedule);
        assertEquals(1L, schedule.getId());
        assertEquals(veterinarian, schedule.getVeterinarian());
        assertEquals(DayOfWeek.MONDAY, schedule.getDayOfWeek());
        assertEquals(LocalTime.of(9, 0), schedule.getStartTime());
        assertEquals(LocalTime.of(17, 0), schedule.getEndTime());
        assertEquals(LocalTime.of(12, 0), schedule.getBreakStartTime());
        assertEquals(LocalTime.of(13, 0), schedule.getBreakEndTime());
    }
}

