package ua.edu.ukma.objectanalysis.openvet.domain.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.Identifiable;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements Identifiable<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 60)
    private String firstName;

    @Column(nullable = false, length = 60)
    private String lastName;

    @Column(nullable = false, unique = true, length = 40)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, insertable = false, updatable = false)
    private UserRole role;
}
