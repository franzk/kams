package net.franzka.kams.authentication.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import net.franzka.kams.authentication.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${net.franzka.kams.authentication.jwt-secret}")
    private String secret;

    @Value("${net.franzka.kams.authentication.jwt.expiration-in-seconds}")
    private int jwtExpirationInS;

    @Override
    public String generateToken(String email) {

        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (30000L)))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    protected Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
