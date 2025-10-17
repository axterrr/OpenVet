package ua.edu.ukma.objectanalysis.openvet.dto.examination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.LabTestType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabResultResponse {
    private Long id;
    private Long medicalRecordId;
    private LabTestType testType;
    private String resultDetails;
    private String notes;
}
