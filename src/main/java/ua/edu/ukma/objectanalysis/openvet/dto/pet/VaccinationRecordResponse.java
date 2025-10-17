package ua.edu.ukma.objectanalysis.openvet.dto.pet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationRecordResponse {
    private Long id;
    private String vaccineName;
    private String manufacturer;
    private String batchNumber;
    private String notes;
    private LocalDate vaccinationDate;
    private LocalDate nextDueDate;
    private Long veterinarianId;
}

