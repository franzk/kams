package net.franzka.kams.authentication.service;

import lombok.extern.log4j.Log4j2;
import net.franzka.kams.authentication.dto.AuthRequestDto;
import net.franzka.kams.authentication.dto.AuthTokenDto;
import net.franzka.kams.authentication.exception.ExpiredTokenException;
import net.franzka.kams.authentication.exception.UserAlreadyExistsException;
import net.franzka.kams.authentication.model.User;
import net.franzka.kams.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthTokenDto generateToken(String username) {
        AuthTokenDto authTokenDto = new AuthTokenDto();
        authTokenDto.setAuthToken(jwtService.generateToken(username));
        return authTokenDto;
    }

    public void validateToken(String token) throws ExpiredTokenException {
        jwtService.validateToken(token);
    }

    @Value("${user.creation.success}")
    private String userCreationSuccess;

    public String createUser(AuthRequestDto authRequestDto) throws UserAlreadyExistsException {
        if (userRepository.findByEmail(authRequestDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        else {
            User user = new User();
            user.setEmail(authRequestDto.getUsername());
            user.setPassword(passwordEncoder.encode(authRequestDto.getPassword()));
            userRepository.save(user);
            return userCreationSuccess;
        }
    }
}
