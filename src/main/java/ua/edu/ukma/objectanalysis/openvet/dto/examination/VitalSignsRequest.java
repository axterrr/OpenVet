package ua.edu.ukma.objectanalysis.openvet.dto.examination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VitalSignsRequest {

    @Range(max = 100, message = "Respiratory rate must be between 0 and 100")
    private Integer respiratoryRate;

    @Range(max = 500, message = "Weight must be between 0 and 500")
    private Double weight;

    @Range(max = 50, message = "Temperature must be between 0 and 50")
    private Double temperature;

    @Range(max = 200, message = "Blood pressure must be between 0 and 200")
    private Double bloodPressure;

    @Range(max = 200, message = "Heart rate must be between 0 and 200")
    private Integer heartRate;
}
