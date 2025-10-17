package ua.edu.ukma.objectanalysis.openvet.dto.examination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionResponse {
    private Long id;
    private Long medicalRecordId;
    private String medicineName;
    private String dosage;
    private String frequency;
    private String duration;
    private String instructions;
}
