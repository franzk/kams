package net.franzka.kams.authentication.service;

import net.bytebuddy.utility.RandomString;
import net.franzka.kams.authentication.dto.AuthRequestDto;
import net.franzka.kams.authentication.exception.ExpiredTokenException;
import net.franzka.kams.authentication.exception.UserAlreadyExistsException;
import net.franzka.kams.authentication.model.User;
import net.franzka.kams.authentication.repository.UserRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
class AuthServiceTest {

    @InjectMocks
    private AuthService serviceUnderTest;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void generateTokenTest() {
        // Arrange
        String testUsername = RandomString.make(64);
        // Act
        serviceUnderTest.generateToken(testUsername);
        // Assert
        verify(jwtService, times(1)).generateToken(testUsername);
    }

    @Test
    void validateTokenTest() throws ExpiredTokenException {
        // Arrange
        String testToken = RandomString.make(64);
        // Act
        serviceUnderTest.validateToken(testToken);
        // Assert
        verify(jwtService, times(1)).validateToken(testToken);
    }

    @Test
    void validateTokenWithExpiredTokenExceptionTest() throws ExpiredTokenException {
        // Arrange
        String testToken = RandomString.make(64);
        doThrow(ExpiredTokenException.class).when(jwtService).validateToken(testToken);
        // Act + Assert
        assertThrows(ExpiredTokenException.class, () -> serviceUnderTest.validateToken(testToken));
    }

    @Test
    void createUserTest() throws UserAlreadyExistsException {
        // Arrange
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        // Act
        serviceUnderTest.createUser(new AuthRequestDto());
        // Assert
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void createUserWithUserAlreadyExistsExceptionTest() {
        // Arrange
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User()));
        // Act + Assert
        assertThrows(UserAlreadyExistsException.class, () -> serviceUnderTest.createUser(new AuthRequestDto()));
    }

}
