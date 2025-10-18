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
        ScheduleEntity schedule = new ScheduleEntity();
        schedule.setId(1L);
        schedule.setDayOfWeek(DayOfWeek.MONDAY);
        schedule.setStartTime(LocalTime.of(9, 0));
        schedule.setEndTime(LocalTime.of(17, 0));
        schedule.setBreakStartTime(LocalTime.of(12, 0));
        schedule.setBreakEndTime(LocalTime.of(13, 0));

        VeterinarianEntity veterinarian = new VeterinarianEntity();
        veterinarian.setId(1L);
        schedule.setVeterinarian(veterinarian);

        assertEquals(1L, schedule.getId());
        assertEquals(DayOfWeek.MONDAY, schedule.getDayOfWeek());
        assertEquals(LocalTime.of(9, 0), schedule.getStartTime());
        assertEquals(LocalTime.of(17, 0), schedule.getEndTime());
        assertEquals(LocalTime.of(12, 0), schedule.getBreakStartTime());
        assertEquals(LocalTime.of(13, 0), schedule.getBreakEndTime());
        assertEquals(veterinarian, schedule.getVeterinarian());
    }

    @Test
    void testScheduleEntityBuilder() {
        VeterinarianEntity veterinarian = new VeterinarianEntity();
        veterinarian.setId(1L);

        ScheduleEntity schedule = ScheduleEntity.builder()
                .id(1L)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(17, 0))
                .breakStartTime(LocalTime.of(12, 0))
                .breakEndTime(LocalTime.of(13, 0))
                .veterinarian(veterinarian)
                .build();

        assertEquals(1L, schedule.getId());
        assertEquals(DayOfWeek.MONDAY, schedule.getDayOfWeek());
        assertEquals(LocalTime.of(9, 0), schedule.getStartTime());
        assertEquals(LocalTime.of(17, 0), schedule.getEndTime());
        assertEquals(LocalTime.of(12, 0), schedule.getBreakStartTime());
        assertEquals(LocalTime.of(13, 0), schedule.getBreakEndTime());
        assertEquals(veterinarian, schedule.getVeterinarian());
    }

    @Test
    void testAllArgsConstructor() {
        VeterinarianEntity veterinarian = new VeterinarianEntity();
        veterinarian.setId(1L);

        ScheduleEntity schedule = new ScheduleEntity(1L, veterinarian, DayOfWeek.MONDAY, LocalTime.of(9, 0),
                LocalTime.of(17, 0), LocalTime.of(12, 0), LocalTime.of(13, 0));

        assertEquals(1L, schedule.getId());
        assertEquals(DayOfWeek.MONDAY, schedule.getDayOfWeek());
        assertEquals(LocalTime.of(9, 0), schedule.getStartTime());
        assertEquals(LocalTime.of(17, 0), schedule.getEndTime());
        assertEquals(LocalTime.of(12, 0), schedule.getBreakStartTime());
        assertEquals(LocalTime.of(13, 0), schedule.getBreakEndTime());
        assertEquals(veterinarian, schedule.getVeterinarian());
    }

    @Test
    void testNoArgsConstructor() {
        ScheduleEntity schedule = new ScheduleEntity();
        assertNotNull(schedule);
    }
}

