package ua.edu.ukma.objectanalysis.openvet.domain.entity.pet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.Identifiable;

import java.time.LocalDateTime;
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
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number", nullable = false, unique = true, length = 15)
    private String phoneNumber;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "pendingOwner")
    private Set<PetEntity> unattachedPets;
}
