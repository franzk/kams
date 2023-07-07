package net.franzka.kams.authentication.service;

public interface JwtService {

    String generateToken(String email);

}
