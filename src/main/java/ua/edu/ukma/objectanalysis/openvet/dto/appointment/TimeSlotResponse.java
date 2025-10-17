package ua.edu.ukma.objectanalysis.openvet.dto.appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.TimeSlotStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotResponse {
    private Long id;
    private Long veterinarianId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private TimeSlotStatus status;
    private Long appointmentId;
}
