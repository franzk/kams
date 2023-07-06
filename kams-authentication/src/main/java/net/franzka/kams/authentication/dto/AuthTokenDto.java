package net.franzka.kams.authentication.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthTokenDto {

    public AuthTokenDto(String authToken) {
        this.authToken = authToken;
    }

    String authToken;

}
