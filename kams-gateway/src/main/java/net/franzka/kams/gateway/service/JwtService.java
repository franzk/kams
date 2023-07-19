package net.franzka.kams.gateway.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import net.franzka.kams.gateway.exception.ExpiredTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;

import java.security.Key;

@Service
@Log4j2
public class JwtService {

    @Value("${net.franzka.kams.authentication.jwt-secret}")
    private String secret;

    public void validateToken(String token) throws ExpiredTokenException {
        log.info("Validating token : " + token);
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
        } catch (ExpiredJwtException ex) {
            log.info("Token Expired : " + token);
            throw new ExpiredTokenException();
        }
        log.info("Token validated : " + token);
    }

    public Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}