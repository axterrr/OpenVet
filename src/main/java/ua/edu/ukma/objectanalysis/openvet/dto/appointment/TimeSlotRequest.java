package ua.edu.ukma.objectanalysis.openvet.dto.appointment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.TimeSlotStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotRequest {
    @NotNull
    private Long veterinarianId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private TimeSlotStatus status;
}
