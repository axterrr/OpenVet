package ua.edu.ukma.objectanalysis.openvet.domain.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AppointmentStatusTest {

    @Test
    void testEnumValues() {
        assertEquals("SCHEDULED", AppointmentStatus.SCHEDULED.name());
        assertEquals("COMPLETED", AppointmentStatus.COMPLETED.name());
        assertEquals("CANCELED_BY_OWNER", AppointmentStatus.CANCELED_BY_OWNER.name());
        assertEquals("CANCELED_BY_CLINIC", AppointmentStatus.CANCELED_BY_CLINIC.name());
        assertEquals("NO_SHOW", AppointmentStatus.NO_SHOW.name());
    }
}

