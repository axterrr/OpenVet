package ua.edu.ukma.objectanalysis.openvet.domain.entity.pet;

import org.junit.jupiter.api.Test;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.PetOwnerEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.PetSex;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PetEntityTest {

    @Test
    void testPetEntity() {
        PetOwnerEntity owner = new PetOwnerEntity();
        owner.setId(1L);

        PetEntity pet = new PetEntity();
        pet.setId(1L);
        pet.setName("Buddy");
        pet.setSpecies("Dog");
        pet.setBreed("Golden Retriever");
        pet.setBirthDate(LocalDate.of(2020, 1, 1));
        pet.setSex(PetSex.MALE);
        pet.setColor("Golden");
        pet.setIsNeutered(true);
        pet.setMicrochipNumber("123456789012345");
        pet.setOwner(owner);
        pet.setVaccinationRecords(new ArrayList<>());

        assertNotNull(pet);
        assertEquals(1L, pet.getId());
        assertEquals("Buddy", pet.getName());
        assertEquals("Dog", pet.getSpecies());
        assertEquals("Golden Retriever", pet.getBreed());
        assertEquals(LocalDate.of(2020, 1, 1), pet.getBirthDate());
        assertEquals(PetSex.MALE, pet.getSex());
        assertEquals("Golden", pet.getColor());
        assertEquals(true, pet.getIsNeutered());
        assertEquals("123456789012345", pet.getMicrochipNumber());
        assertEquals(owner, pet.getOwner());
        assertNotNull(pet.getVaccinationRecords());
    }
}

