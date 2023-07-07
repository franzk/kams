package net.franzka.kams.authentication.service;

import net.franzka.kams.authentication.model.User;

public interface JwtService {

    String generateToken(String email);

    //void validateToken(String authToken);

}
