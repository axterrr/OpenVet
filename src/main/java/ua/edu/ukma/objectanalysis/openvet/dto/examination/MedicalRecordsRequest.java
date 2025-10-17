package ua.edu.ukma.objectanalysis.openvet.dto.examination;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordsRequest {

    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;

    @NotBlank(message = "Diagnosis is required")
    @Size(max = 150, message = "Diagnosis is too large")
    private String diagnosis;

    @Size(max = 300, message = "Treatment is too large")
    private String treatment;

    @Size(max = 2000, message = "Notes is too large")
    private String notes;

    @Valid
    private VitalSignsRequest vitalSigns;

    @Valid
    private List<PrescriptionRequest> prescriptions;

    @Valid
    private List<LabResultRequest> labResults;
}
