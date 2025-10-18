package ua.edu.ukma.objectanalysis.openvet.domain.entity.user;

import org.junit.jupiter.api.Test;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserEntityTest {

    @Test
    void testUserEntity() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhoneNumber("1234567890");
        user.setRole(UserRole.ADMIN);

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("1234567890", user.getPhoneNumber());
        assertEquals(UserRole.ADMIN, user.getRole());
    }
}

