package ua.edu.ukma.objectanalysis.openvet.dto.pet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.PastOrPresent;
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
public class PetRequest {

    @NotBlank(message = "Pet name is required")
    @Size(max = 60, message = "Pet name is too large")
    private String name;

    @NotBlank(message = "Pet species is required")
    @Size(max = 60, message = "Pet species is too large")
    private String species;

    @Size(max = 60, message = "Pet species is too large")
    private String breed;

    @PastOrPresent(message = "Birth date cannot be in the future")
    private LocalDate birthDate;

    @NotNull(message = "Pet sex is required")
    private PetSex sex;

    @Size(max = 60, message = "Pet color is too large")
    private String color;

    @NotNull(message = "Pet neutered is required")
    private Boolean neutered;

    @Size(max = 15, message = "Microchip number must be at most 15 characters")
    private String microchipNumber;

    // looks for registered owners; if not found - pending user
    @NotBlank(message = "Pet owner phone is required")
    private String ownerPhone;
}

