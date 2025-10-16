package ua.edu.ukma.objectanalysis.openvet.domain.entity.pet;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.Identifiable;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.PetOwnerEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pets")
public class PetEntity implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String name;

    @Column(length = 60)
    private String species;

    @Column(length = 60)
    private String breed;

    private LocalDate birthDate;

    private String gender;

    private String color;

    private boolean isNeutered;

    @Column(length = 15, unique = true)
    @Size(max = 15)
    private String microchipNumber;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private PetOwnerEntity owner;

    @ManyToOne
    @JoinColumn(name = "pending_owner_id")
    private PendingOwnerEntity pendingOwner;

    @OneToMany(mappedBy = "pet")
    private List<VaccinationRecordEntity> vaccinationRecords = new ArrayList<>();
}
