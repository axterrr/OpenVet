package ua.edu.ukma.objectanalysis.openvet.dto.examination;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.LabTestType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabResultRequest {

    @NotNull(message = "Test type is required")
    private LabTestType testType;

    @NotBlank(message = "Result value is required")
    @Size(max = 1000, message = "Treatment is too large")
    private String resultDetails;

    @Size(max = 1000, message = "Notes is too large")
    private String notes;
}
