package net.franzka.kams.authentication.controller;

import net.franzka.kams.authentication.dto.AuthRequestDto;
import net.franzka.kams.authentication.dto.AuthTokenDto;
import net.franzka.kams.authentication.exception.ExpiredTokenException;
import net.franzka.kams.authentication.exception.InvalidAccessException;
import net.franzka.kams.authentication.exception.UserAlreadyExistsException;
import net.franzka.kams.authentication.service.AuthService;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
class AuthControllerTest {

    @InjectMocks
    AuthController controllerUnderTest;

    @Mock
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    void addNewUserTest() throws UserAlreadyExistsException {
        // Arrange
        String expectedResult = RandomString.make(64);
        when(authService.createUser(any())).thenReturn(expectedResult);
        // Act
        ResponseEntity<String> result = controllerUnderTest.addNewUser(new AuthRequestDto());
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isEqualTo(expectedResult);
    }

    @Test
    void addNewUserWithUserAlreadyExistsExceptionTest() throws UserAlreadyExistsException {
        // Arrange
        when(authService.createUser(any())).thenThrow(UserAlreadyExistsException.class);
        // Act + Assert
        assertThrows(UserAlreadyExistsException.class, () -> controllerUnderTest.addNewUser(new AuthRequestDto()));
    }

    @Test
    void getTokenTest() throws InvalidAccessException {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        // Act
        ResponseEntity<AuthTokenDto> result = controllerUnderTest.getToken(new AuthRequestDto());
        // Assert
        verify(authService, times(1)).generateToken(any());
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    void getTokenWithInvalidAccessExceptionTest() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);
        // Act + Assert
        assertThrows(InvalidAccessException.class, () -> controllerUnderTest.getToken(new AuthRequestDto()));
    }

    @Test
    void validateTokenTest() throws ExpiredTokenException {
        // Arrange
        String testToken = RandomString.make(64);
        // Act
        controllerUnderTest.validateToken(testToken);
        // Assert
        verify(authService, times(1)).validateToken(testToken);
    }

    @Test
    void validateTokenWithExpiredTokenExceptionTest() throws ExpiredTokenException {
        // Arrange
        String testToken = RandomString.make(64);
        doThrow(ExpiredTokenException.class).when(authService).validateToken(testToken);
        // Act + Assert
        assertThrows(ExpiredTokenException.class, () -> authService.validateToken(testToken));
    }


}
