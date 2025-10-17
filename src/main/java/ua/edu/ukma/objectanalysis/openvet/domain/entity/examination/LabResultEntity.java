package ua.edu.ukma.objectanalysis.openvet.domain.entity.examination;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.Identifiable;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.LabTestType;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lab_results")
public class LabResultEntity implements Identifiable<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medical_record_id", updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MedicalRecordsEntity medicalRecord;

    @Enumerated(EnumType.STRING)
    @Column(name = "test_type", nullable = false)
    private LabTestType testType;

    @Column(name = "result_details", nullable = false, length = 1000)
    private String resultDetails;

    @Column(name = "notes", length = 1000)
    private String notes;
}
