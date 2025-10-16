package ua.edu.ukma.objectanalysis.openvet.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "User first name cannot be blank")
    @Size(max = 60, message = "User first name is too large")
    private String firstName;

    @NotBlank(message = "User last name cannot be blank")
    @Size(max = 60, message = "User last name is too large")
    private String lastName;

    @NotBlank(message = "User phone number cannot be blank")
    @Pattern(regexp = "^\\+?\\d{10,15}$", message = "Invalid user phone number")
    private String phone;

    @Email(message = "User email is in wrong format")
    @Size(max = 120, message = "User email is too large")
    private String email;

    @NotBlank(message = "User password cannot be blank")
    @Size(min = 8, message = "Password length should be at least 8 symbols")
    private String password;

    @NotEmpty(message = "User role cannot be empty")
    private UserRole role;
}
