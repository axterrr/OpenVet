package ua.edu.ukma.objectanalysis.openvet.domain.entity.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;

@Entity
@DiscriminatorValue(UserRole.PET_OWNER_DISC)
public class PetOwner extends UserEntity {
}
