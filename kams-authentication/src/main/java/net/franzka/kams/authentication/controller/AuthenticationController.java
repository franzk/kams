package net.franzka.kams.authentication.controller;

import lombok.extern.log4j.Log4j2;
import net.franzka.kams.authentication.dto.AuthTokenDto;
import net.franzka.kams.authentication.dto.UserDto;
import net.franzka.kams.authentication.service.AuthenticationService;
import net.franzka.kams.authentication.service.impl.AuthenticationServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@CrossOrigin
@RequestMapping("/auth")
public class AuthenticationController {

    private AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationController(AuthenticationServiceImpl authenticationServiceImpl, AuthenticationManager authenticationManager) {
        this.authenticationService = authenticationServiceImpl;
        this.authenticationManager = authenticationManager;
    }

    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthTokenDto> login(@RequestBody UserDto userDto) throws BadCredentialsException {
        log.info("Authentication : " + userDto.getEmail());

        String username = userDto.getEmail();
        String password = userDto.getPassword();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authenticationManager.authenticate(token);

        if (authentication.isAuthenticated()) {
            return new ResponseEntity<>(authenticationService.generateToken(userDto.getEmail()), HttpStatus.OK);
        } else {
            throw new BadCredentialsException("");
        }
    }

//    @GetMapping("/aa")
//    public ResponseEntity<String> oauth2Login(OAuth2AuthenticationToken authentication) {
//        return new ResponseEntity<>(authentication.toString(), HttpStatus.OK);
//    }

}
