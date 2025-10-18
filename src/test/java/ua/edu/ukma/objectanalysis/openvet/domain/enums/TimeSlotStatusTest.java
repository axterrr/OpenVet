package ua.edu.ukma.objectanalysis.openvet.domain.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeSlotStatusTest {

    @Test
    void testEnumValues() {
        assertEquals("AVAILABLE", TimeSlotStatus.AVAILABLE.name());
        assertEquals("BOOKED", TimeSlotStatus.BOOKED.name());
        assertEquals("UNAVAILABLE", TimeSlotStatus.UNAVAILABLE.name());
    }
}

