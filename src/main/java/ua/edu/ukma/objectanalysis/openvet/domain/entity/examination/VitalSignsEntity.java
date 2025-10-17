package ua.edu.ukma.objectanalysis.openvet.domain.entity.examination;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.Identifiable;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vital_signs")
public class VitalSignsEntity implements Identifiable<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "vitalSigns", optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MedicalRecordsEntity medicalRecord;

    @Column(name = "respiration_rate")
    private Integer respiratoryRate;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "blood_pressure")
    private Double bloodPressure;

    @Column(name = "heart_rate")
    private Integer heartRate;
}
