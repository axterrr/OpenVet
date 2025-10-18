package ua.edu.ukma.objectanalysis.openvet.domain.entity.pet;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PendingOwnerEntityTest {

    @Test
    void testPendingOwnerEntity() {
        PendingOwnerEntity pendingOwner = new PendingOwnerEntity();
        pendingOwner.setId(1L);
        pendingOwner.setPhoneNumber("1234567890");
        pendingOwner.setCreatedDate(LocalDateTime.now());
        pendingOwner.setUnattachedPets(new HashSet<>());

        assertNotNull(pendingOwner);
        assertEquals(1L, pendingOwner.getId());
        assertEquals("1234567890", pendingOwner.getPhoneNumber());
        assertNotNull(pendingOwner.getCreatedDate());
        assertNotNull(pendingOwner.getUnattachedPets());
    }
}

