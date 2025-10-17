package ua.edu.ukma.objectanalysis.openvet.dto.examination;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionRequest {

    @NotBlank(message = "Medicine name is required")
    @Size(max = 100, message = "Medicine name is too large")
    private String medicineName;

    @NotBlank(message = "Medicine dosage is required")
    @Size(max = 50, message = "Medicine dosage is too large")
    private String dosage;

    @NotBlank(message = "Medicine frequency is required")
    @Size(max = 100, message = "Medicine frequency is too large")
    private String frequency;

    @Size(max = 50, message = "Medicine duration is too large")
    private String duration;

    @Size(max = 500, message = "Medicine instructions is too large")
    private String instructions;
}
