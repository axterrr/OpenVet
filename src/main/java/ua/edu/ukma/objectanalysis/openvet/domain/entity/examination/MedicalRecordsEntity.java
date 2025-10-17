package ua.edu.ukma.objectanalysis.openvet.domain.entity.examination;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.Identifiable;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.AppointmentEntity;

import java.util.Set;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "medical_records")
public class MedicalRecordsEntity implements Identifiable<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "appointment_id", updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AppointmentEntity appointment;

    @Column(name = "diagnosis", length = 150, nullable = false)
    private String diagnosis;

    @Column(name = "treatment", length = 300)
    private String treatment;

    @Column(name = "notes", length = 2000)
    private String notes;

    @OneToOne(mappedBy = "medicalRecord", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private VitalSignsEntity vitalSigns;

    @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PrescriptionEntity> prescriptions;

    @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LabResultEntity> labResults;

    // TODO: follow up appointment?
}
