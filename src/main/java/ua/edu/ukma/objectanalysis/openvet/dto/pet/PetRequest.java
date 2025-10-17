package ua.edu.ukma.objectanalysis.openvet.dto.pet;

import jakarta.validation.constraints.NotBlank;
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
    private String name;

    private String species;

    private String breed;

    @PastOrPresent(message = "Birth date cannot be in the future")
    private LocalDate birthDate;

    private PetSex sex;

    private String color;

    // boxed to allow partial update semantics
    private Boolean neutered;

    @Size(max = 15, message = "Microchip number must be at most 15 characters")
    private String microchipNumber;

    // Optional: link to an existing owner
    private Long ownerId;

    // Optional: link to a pending owner by phone number (when owner isn't registered yet)
    @Size(max = 15, message = "Phone number must be at most 15 characters")
    private String pendingOwnerPhone;
}

