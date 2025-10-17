package ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import ua.edu.ukma.objectanalysis.openvet.domain.entity.examination.MedicalRecordsEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PetEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.AppointmentStatus;

import java.util.Set;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "appointments")
public class AppointmentEntity implements Identifiable<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private PetEntity pet;

    /*
     * TODO: How should we handle time slots?
     *   - Be able to edit timeslots, like change start-end? Unite time slots? [?]
     *   - @OneToMany and allow multiple appointments in a timeslot?
     */
    @OneToOne
    @JoinColumn(name = "time_slot_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private TimeSlotEntity timeSlot;

    @Column(name = "reason_for_visit", nullable = false)
    private String reasonForVisit;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status;

    @Column(name = "notes", length = 2000)
    private String notes;

    @OneToMany(mappedBy = "appointment")
    private Set<MedicalRecordsEntity> medicalRecords;
}
