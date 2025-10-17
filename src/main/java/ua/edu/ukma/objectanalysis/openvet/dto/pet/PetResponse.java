package ua.edu.ukma.objectanalysis.openvet.dto.pet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.PetSex;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetResponse {
    private Long id;
    private String name;
    private String species;
    private String breed;
    private LocalDate birthDate;
    private PetSex sex;
    private String color;
    private Boolean neutered;
    private String microchipNumber;
    private Long ownerId;
    private Long pendingOwnerId;
}
