package ua.edu.ukma.objectanalysis.openvet.domain.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LabTestTypeTest {

    @Test
    void testEnumValues() {
        assertEquals("BLOOD_TEST", LabTestType.BLOOD_TEST.name());
        assertEquals("URINE_TEST", LabTestType.URINE_TEST.name());
        assertEquals("XRAY", LabTestType.XRAY.name());
        assertEquals("MRI", LabTestType.MRI.name());
        assertEquals("ULTRASOUND", LabTestType.ULTRASOUND.name());
        assertEquals("BIOCHEMICAL_ANALYSIS", LabTestType.BIOCHEMICAL_ANALYSIS.name());
        assertEquals("HORMONE_TEST", LabTestType.HORMONE_TEST.name());
        assertEquals("DNA_TEST", LabTestType.DNA_TEST.name());
    }
}

