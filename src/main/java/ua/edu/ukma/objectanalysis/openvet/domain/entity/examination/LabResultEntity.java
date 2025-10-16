package ua.edu.ukma.objectanalysis.openvet.domain.entity.examination;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.Identifiable;

import java.time.LocalDate;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lab_results")
public class LabResultEntity implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_record_id", nullable = false)
    private MedicalRecordsEntity medicalRecord;

    @Column(nullable = false, length = 100)
    private String testType;

    @Column(nullable = false)
    private String resultDetails;

    @Column(nullable = false)
    private LocalDate resultDate;

    private String notes;
}
