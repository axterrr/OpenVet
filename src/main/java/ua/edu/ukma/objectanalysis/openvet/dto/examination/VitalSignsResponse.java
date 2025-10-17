package ua.edu.ukma.objectanalysis.openvet.dto.examination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VitalSignsResponse {
    private Long id;
    private Long medicalRecordId;
    private Integer respiratoryRate;
    private Double weight;
    private Double temperature;
    private Double bloodPressure;
    private Integer heartRate;
}
