package net.franzka.kams.authentication.controller;

import lombok.extern.log4j.Log4j2;
import net.franzka.kams.authentication.dto.AuthRequestDto;
import net.franzka.kams.authentication.dto.AuthTokenDto;
import net.franzka.kams.authentication.exception.ExpiredTokenException;
import net.franzka.kams.authentication.exception.InvalidAccessException;
import net.franzka.kams.authentication.exception.UserAlreadyExistsException;
import net.franzka.kams.authentication.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Log4j2
@PropertySource("classpath:messages.properties")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;


    public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<String> addNewUser(@RequestBody AuthRequestDto authRequestDto) throws UserAlreadyExistsException {
        log.info("Start registering new user :" + authRequestDto.getUsername());

        return new ResponseEntity<>(authService.createUser(authRequestDto), HttpStatus.CREATED);
    }

    @PostMapping("/token")
    public ResponseEntity<AuthTokenDto> getToken(@RequestBody AuthRequestDto authRequestDto) throws InvalidAccessException {
        log.info("Token requested for : " + authRequestDto.getUsername());

        String username = authRequestDto.getUsername();
        String password = authRequestDto.getPassword();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authenticationManager.authenticate(token);
        if (authentication.isAuthenticated()) {
            return new ResponseEntity<>(authService.generateToken(authRequestDto.getUsername()), HttpStatus.OK);
        } else {
            throw new InvalidAccessException();
        }
    }

    @Value("${token.valid}")
    private String validTokenMessage;
    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam("token") String token) throws ExpiredTokenException {
        log.info("Start Token validation : " + token);

        authService.validateToken(token);
        return new ResponseEntity<>(validTokenMessage, HttpStatus.ACCEPTED);
    }


}
