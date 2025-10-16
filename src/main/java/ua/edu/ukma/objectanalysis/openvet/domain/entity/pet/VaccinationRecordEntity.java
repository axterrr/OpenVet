package ua.edu.ukma.objectanalysis.openvet.domain.entity.pet;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.Identifiable;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.VeterinarianEntity;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vaccination_records")
public class VaccinationRecordEntity implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String vaccineName;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private String batchNumber;

    @ManyToOne
    @JoinColumn(name = "veterinarian_id", nullable = false)
    private VeterinarianEntity veterinarian;

    @Column(length = 2000)
    private String notes;

    @Column(nullable = false)
    private java.time.LocalDate vaccinationDate;

    private java.time.LocalDate nextDueDate;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private PetEntity pet;
}
