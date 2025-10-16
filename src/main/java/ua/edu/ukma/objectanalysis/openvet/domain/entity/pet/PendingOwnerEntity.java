package ua.edu.ukma.objectanalysis.openvet.domain.entity.pet;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.Identifiable;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a temporary linkage for a Pet using a phone number
 * when the PetOwner is not yet registered in the system.
 * This entity will be deleted once a PetOwner registers with the matching phone number.
 */

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pending_owners")
public class PendingOwnerEntity implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 15)
    private String phoneNumber;

    @Column(nullable = false)
    private final LocalDateTime createdDate = LocalDateTime.now();

    @OneToMany(mappedBy = "pendingOwner")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Set<PetEntity> unattachedPets = new HashSet<>();
}
