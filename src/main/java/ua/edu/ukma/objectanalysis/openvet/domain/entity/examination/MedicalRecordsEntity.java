package ua.edu.ukma.objectanalysis.openvet.domain.entity.examination;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.Identifiable;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.AppointmentEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PetEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.VeterinarianEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "medical_records")
public class MedicalRecordsEntity implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "pet_id")
    private PetEntity pet;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "appointment_id")
    private AppointmentEntity appointment;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "veterinarian_id")
    private VeterinarianEntity veterinarian;

    @Column(length = 150)
    private String diagnosis;

    @Column(length = 300)
    private String treatment;

    @Column(length = 2000)
    private String notes;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vital_signs_id")
    private VitalSignsEntity vitalSigns;

    @OneToMany(mappedBy = "medicalRecord", fetch = FetchType.LAZY)
    private Set<PrescriptionEntity> prescriptions = new HashSet<>();

    @OneToMany(mappedBy = "medicalRecord", fetch = FetchType.LAZY)
    private Set<LabResultEntity> labResults = new HashSet<>();

    LocalDate examinationDate;

    LocalDateTime createdDate = LocalDateTime.now();

    // TODO: follow up appointment?
}
