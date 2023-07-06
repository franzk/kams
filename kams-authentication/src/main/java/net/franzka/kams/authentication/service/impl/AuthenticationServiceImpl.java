package net.franzka.kams.authentication.service.impl;

import net.franzka.kams.authentication.dto.AuthTokenDto;
import net.franzka.kams.authentication.service.AuthenticationService;
import net.franzka.kams.authentication.service.JwtService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl  implements AuthenticationService {

    private final JwtService jwtService;

    public AuthenticationServiceImpl(JwtServiceImpl jwtServiceImpl) {
        this.jwtService = jwtServiceImpl;
    }

    public AuthTokenDto generateToken(String email) {
        return new AuthTokenDto(jwtService.generateToken(email));
    }
}
