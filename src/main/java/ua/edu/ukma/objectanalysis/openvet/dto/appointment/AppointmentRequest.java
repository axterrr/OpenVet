package ua.edu.ukma.objectanalysis.openvet.dto.appointment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.AppointmentStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {

    private Long petId;

    private Long timeSlotId;

    private String reasonForVisit;

    private AppointmentStatus status;

    private String notes;
}

