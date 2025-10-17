package ua.edu.ukma.objectanalysis.openvet.domain.entity.pet;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.Identifiable;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.VeterinarianEntity;

import java.time.LocalDate;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vaccination_records")
public class VaccinationRecordEntity implements Identifiable<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vaccine_name", nullable = false)
    private String vaccineName;

    @Column(name = "manufacturer", nullable = false)
    private String manufacturer;

    @Column(name = "batch_number", nullable = false)
    private String batchNumber;

    @ManyToOne
    @JoinColumn(name = "veterinarian_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private VeterinarianEntity veterinarian;

    @Column(name = "notes", length = 2000)
    private String notes;

    @Column(name = "vaccination_date", nullable = false)
    private LocalDate vaccinationDate;

    @Column(name = "next_due_date", nullable = false)
    private LocalDate nextDueDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pet_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PetEntity pet;
}
