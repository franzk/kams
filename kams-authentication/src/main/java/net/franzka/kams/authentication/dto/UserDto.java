package net.franzka.kams.authentication.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO used to transfer user infos
 */
@Getter
@Setter
public class UserDto {

    String email;
    String password;
    String role;

}
