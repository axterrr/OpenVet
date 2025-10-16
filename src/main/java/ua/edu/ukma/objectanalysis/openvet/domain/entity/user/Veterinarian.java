package ua.edu.ukma.objectanalysis.openvet.domain.entity.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("VETERINARIAN")
public class Veterinarian extends UserEntity {
}
