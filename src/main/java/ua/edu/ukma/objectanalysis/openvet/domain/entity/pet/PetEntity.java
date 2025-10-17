package ua.edu.ukma.objectanalysis.openvet.domain.entity.pet;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.Identifiable;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.PetOwnerEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.PetSex;

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
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 60)
    private String name;

    @Column(name = "species", nullable = false, length = 60)
    private String species;

    @Column(name = "breed", length = 60)
    private String breed;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex", nullable = false)
    private PetSex sex;

    @Column(name = "color")
    private String color;

    @Column(name = "is_neutered", nullable = false)
    private boolean isNeutered;

    @Column(name = "microchip_number", length = 15, unique = true)
    private String microchipNumber;

    @ManyToOne
    @JoinColumn(name = "owner_id", updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PetOwnerEntity owner;

    @ManyToOne
    @JoinColumn(name = "pending_owner_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private PendingOwnerEntity pendingOwner;

    @OneToMany(mappedBy = "pet", fetch = FetchType.LAZY)
    private List<VaccinationRecordEntity> vaccinationRecords = new ArrayList<>();
}
