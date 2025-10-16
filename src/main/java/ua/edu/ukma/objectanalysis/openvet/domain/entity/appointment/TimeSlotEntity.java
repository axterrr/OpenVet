package ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.Identifiable;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.Veterinarian;
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
        @UniqueConstraint(columnNames = {"veterinarian_id", "startTime"})
})
public class TimeSlotEntity implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinarian_id", nullable = false)
    private Veterinarian veterinarian;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimeSlotStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private AppointmentEntity appointment;
}
