package ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment;

import org.junit.jupiter.api.Test;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.VeterinarianEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.TimeSlotStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TimeSlotEntityTest {

    @Test
    void testTimeSlotEntity() {
        VeterinarianEntity veterinarian = new VeterinarianEntity();
        veterinarian.setId(1L);

        TimeSlotEntity timeSlot = new TimeSlotEntity();
        timeSlot.setId(1L);
        timeSlot.setVeterinarian(veterinarian);
        timeSlot.setStartTime(LocalDateTime.of(2023, 10, 26, 10, 0));
        timeSlot.setEndTime(LocalDateTime.of(2023, 10, 26, 10, 30));
        timeSlot.setStatus(TimeSlotStatus.AVAILABLE);

        assertNotNull(timeSlot);
        assertEquals(1L, timeSlot.getId());
        assertEquals(veterinarian, timeSlot.getVeterinarian());
        assertEquals(LocalDateTime.of(2023, 10, 26, 10, 0), timeSlot.getStartTime());
        assertEquals(LocalDateTime.of(2023, 10, 26, 10, 30), timeSlot.getEndTime());
        assertEquals(TimeSlotStatus.AVAILABLE, timeSlot.getStatus());
    }
}

