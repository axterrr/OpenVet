package ua.edu.ukma.objectanalysis.openvet.domain.entity.user;

import org.junit.jupiter.api.Test;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class VeterinarianEntityTest {

    @Test
    void testVeterinarianEntity() {
        VeterinarianEntity veterinarian = new VeterinarianEntity();
        veterinarian.setId(1L);
        veterinarian.setFirstName("Dr.");
        veterinarian.setLastName("Smith");
        veterinarian.setEmail("drsmith@example.com");
        veterinarian.setPassword("password");
        veterinarian.setRole(UserRole.VETERINARIAN);

        assertNotNull(veterinarian);
        assertEquals(1L, veterinarian.getId());
        assertEquals("Dr.", veterinarian.getFirstName());
        assertEquals("Smith", veterinarian.getLastName());
        assertEquals("drsmith@example.com", veterinarian.getEmail());
        assertEquals("password", veterinarian.getPassword());
        assertEquals(UserRole.VETERINARIAN, veterinarian.getRole());
    }
}
