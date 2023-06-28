package net.franzka.kams.authentication.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import net.franzka.kams.authentication.annotation.Password;

/**
 * DTO used to transfer user infos
 */
@Getter
@Setter
public class UserDto {

    @Email(message = "{net.franzka.kams.authentication.error.invalid-email-format}")
    String email;

    @Password(message = "{net.franzka.kams.authentication.error.invalid-password-format}")
    String password;

    String role;

}
