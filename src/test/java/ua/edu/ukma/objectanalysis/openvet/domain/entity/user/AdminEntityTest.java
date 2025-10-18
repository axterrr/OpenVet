package ua.edu.ukma.objectanalysis.openvet.domain.entity.user;

import org.junit.jupiter.api.Test;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AdminEntityTest {

    @Test
    void testAdminEntity() {
        AdminEntity admin = new AdminEntity();
        admin.setId(1L);
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setEmail("admin@example.com");
        admin.setPassword("password");
        admin.setRole(UserRole.ADMIN);

        assertNotNull(admin);
        assertEquals(1L, admin.getId());
        assertEquals("Admin", admin.getFirstName());
        assertEquals("User", admin.getLastName());
        assertEquals("admin@example.com", admin.getEmail());
        assertEquals("password", admin.getPassword());
        assertEquals(UserRole.ADMIN, admin.getRole());
    }
}
