package ua.edu.ukma.objectanalysis.openvet.domain.entity.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;

@Entity
@DiscriminatorValue(UserRole.ADMIN_DISC)
public class Admin extends UserEntity {
}
