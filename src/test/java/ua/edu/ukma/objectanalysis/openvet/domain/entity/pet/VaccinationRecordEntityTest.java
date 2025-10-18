package ua.edu.ukma.objectanalysis.openvet.domain.entity.pet;

import org.junit.jupiter.api.Test;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.VeterinarianEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class VaccinationRecordEntityTest {

    @Test
    void testVaccinationRecordEntity() {
        PetEntity pet = new PetEntity();
        pet.setId(1L);

        VeterinarianEntity veterinarian = new VeterinarianEntity();
        veterinarian.setId(1L);

        VaccinationRecordEntity vaccinationRecord = new VaccinationRecordEntity();
        vaccinationRecord.setId(1L);
        vaccinationRecord.setVaccineName("Rabies");
        vaccinationRecord.setManufacturer("Pfizer");
        vaccinationRecord.setBatchNumber("12345");
        vaccinationRecord.setVeterinarian(veterinarian);
        vaccinationRecord.setNotes("No reaction.");
        vaccinationRecord.setVaccinationDate(LocalDate.of(2023, 1, 1));
        vaccinationRecord.setNextDueDate(LocalDate.of(2024, 1, 1));
        vaccinationRecord.setPet(pet);

        assertNotNull(vaccinationRecord);
        assertEquals(1L, vaccinationRecord.getId());
        assertEquals("Rabies", vaccinationRecord.getVaccineName());
        assertEquals("Pfizer", vaccinationRecord.getManufacturer());
        assertEquals("12345", vaccinationRecord.getBatchNumber());
        assertEquals(veterinarian, vaccinationRecord.getVeterinarian());
        assertEquals("No reaction.", vaccinationRecord.getNotes());
        assertEquals(LocalDate.of(2023, 1, 1), vaccinationRecord.getVaccinationDate());
        assertEquals(LocalDate.of(2024, 1, 1), vaccinationRecord.getNextDueDate());
        assertEquals(pet, vaccinationRecord.getPet());
    }
}

