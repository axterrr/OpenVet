package ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.Identifiable;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.VeterinarianEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.TimeSlotStatus;

import java.time.LocalDateTime;

/*
 * Represents a time slot for a veterinarian. It's like a "window" for an appointment.
 * The default duration for a time slot is 30 minutes, but it can be adjusted.
 * Time slots are generated based on the veterinarian's schedule.
 * Time slots can be booked by creating an appointment linked to the time slot.
 */

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "time_slots", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"veterinarian_id", "start_time"})
})
public class TimeSlotEntity implements Identifiable<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "veterinarian_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private VeterinarianEntity veterinarian;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TimeSlotStatus status;

    @OneToOne
    @JoinColumn(name = "appointment_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private AppointmentEntity appointment;
}
