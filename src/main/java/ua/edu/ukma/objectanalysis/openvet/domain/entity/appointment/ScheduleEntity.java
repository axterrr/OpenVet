package ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.Identifiable;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.Veterinarian;

import java.time.DayOfWeek;
import java.time.LocalTime;

/*
 * Represents a reccuring weekly schedule for a veterinarian
 * Used to generate time slots for a veterinarian
 */

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "schedules", uniqueConstraints =
        @UniqueConstraint(columnNames = {"veterinarian_id", "dayOfWeek"})
)
public class ScheduleEntity implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinarian_id", nullable = false)
    private Veterinarian veterinarian;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    private LocalTime breakStartTime;
    private LocalTime breakEndTime;
}
