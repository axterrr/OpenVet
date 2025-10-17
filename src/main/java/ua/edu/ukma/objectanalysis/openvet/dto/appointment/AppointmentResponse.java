package ua.edu.ukma.objectanalysis.openvet.dto.appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.AppointmentStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private Long id;
    private Long petId;
    private Long veterinarianId;
    private Long timeSlotId;
    private LocalDateTime timeSlotStartTime;
    private LocalDateTime timeSlotEndTime;
    private String reasonForVisit;
    private AppointmentStatus status;
    private String notes;
}

