package net.franzka.kams.authentication.service;

import net.franzka.kams.authentication.dto.AuthTokenDto;

public interface AuthenticationService {

    AuthTokenDto generateToken(String email);

    // TODO refresh Token

}
