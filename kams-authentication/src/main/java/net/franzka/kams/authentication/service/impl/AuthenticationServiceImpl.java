package net.franzka.kams.authentication.service.impl;

import net.franzka.kams.authentication.dto.AuthTokenDto;
import net.franzka.kams.authentication.service.AuthenticationService;
import net.franzka.kams.authentication.service.JwtService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl  implements AuthenticationService {

    private JwtService jwtService;

    public AuthenticationServiceImpl(JwtServiceImpl jwtServiceImpl) {
        this.jwtService = jwtServiceImpl;
    }

    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public AuthTokenDto generateToken(String email) {
        AuthTokenDto result = new AuthTokenDto();
        result.setAuthToken(jwtService.generateToken(email));
        return result;
    }
}
