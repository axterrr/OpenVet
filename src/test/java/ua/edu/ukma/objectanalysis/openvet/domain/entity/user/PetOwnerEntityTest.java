package ua.edu.ukma.objectanalysis.openvet.domain.entity.user;

import org.junit.jupiter.api.Test;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PetOwnerEntityTest {

    @Test
    void testPetOwnerEntity() {
        PetOwnerEntity petOwner = new PetOwnerEntity();
        petOwner.setId(1L);
        petOwner.setFirstName("John");
        petOwner.setLastName("Doe");
        petOwner.setEmail("johndoe@example.com");
        petOwner.setPassword("password");
        petOwner.setRole(UserRole.PET_OWNER);

        assertNotNull(petOwner);
        assertEquals(1L, petOwner.getId());
        assertEquals("John", petOwner.getFirstName());
        assertEquals("Doe", petOwner.getLastName());
        assertEquals("johndoe@example.com", petOwner.getEmail());
        assertEquals("password", petOwner.getPassword());
        assertEquals(UserRole.PET_OWNER, petOwner.getRole());
    }
}
