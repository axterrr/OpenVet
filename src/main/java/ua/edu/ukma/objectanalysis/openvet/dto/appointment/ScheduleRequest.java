package ua.edu.ukma.objectanalysis.openvet.dto.appointment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRequest {
    @NotNull
    private Long veterinarianId;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime breakStartTime;
    private LocalTime breakEndTime;
}

