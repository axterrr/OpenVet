package ua.edu.ukma.objectanalysis.openvet.domain.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PetSexTest {

    @Test
    void testEnumValues() {
        assertEquals("MALE", PetSex.MALE.name());
        assertEquals("FEMALE", PetSex.FEMALE.name());
        assertEquals("OTHER", PetSex.OTHER.name());
    }
}

