package ua.edu.ukma.objectanalysis.openvet.dto.pet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationRecordRequest {
    @NotBlank(message = "Vaccine name is required")
    private String vaccineName;
    @NotBlank(message = "Manufacturer is required")
    private String manufacturer;
    @NotBlank(message = "Batch number is required")
    private String batchNumber;
    private String notes;
    @NotNull(message = "Vaccination date is required")
    private LocalDate vaccinationDate;
    private LocalDate nextDueDate;
}

