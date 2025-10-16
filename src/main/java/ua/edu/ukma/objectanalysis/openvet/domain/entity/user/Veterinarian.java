package ua.edu.ukma.objectanalysis.openvet.domain.entity.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;

@Entity
@DiscriminatorValue(UserRole.VETERINARIAN_DISC)
public class Veterinarian extends UserEntity {
}
