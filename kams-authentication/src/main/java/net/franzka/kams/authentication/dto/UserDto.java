package net.franzka.kams.authentication.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    String email;
    String password;
    String role;

}
