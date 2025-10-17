package ua.edu.ukma.objectanalysis.openvet.dto.examination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordsResponse {
    private Long id;
    private Long appointmentId;
    private String diagnosis;
    private String treatment;
    private String notes;
    private VitalSignsResponse vitalSigns;
    private List<LabResultResponse> labResults;
    private List<PrescriptionResponse> prescriptions;
}
