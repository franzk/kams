package net.franzka.kams.authentication.controller;

import net.bytebuddy.utility.RandomString;
import net.franzka.kams.authentication.dto.AuthTokenDto;
import net.franzka.kams.authentication.dto.UserDto;
import net.franzka.kams.authentication.service.AuthenticationService;
import net.franzka.kams.authentication.utils.GenerateTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
class AuthenticationControllerTest {

    @InjectMocks
    AuthenticationController controllerUnderTest;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    AuthenticationService authenticationService;


    @BeforeEach
    public void setup() {
        controllerUnderTest.setAuthenticationService(authenticationService);
    }

    @Test
    void authenticateTest() throws BadCredentialsException {
        // Arrange
        UserDto testDto = GenerateTestData.generateUserDto();
        AuthTokenDto authTokenDto = new AuthTokenDto();
        authTokenDto.setAuthToken(RandomString.make(64));
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authenticationService.generateToken(testDto.getEmail())).thenReturn(authTokenDto);

        // Act
        ResponseEntity<AuthTokenDto> result = controllerUnderTest.login(testDto);

        // Assert
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getAuthToken()).isEqualTo(authTokenDto.getAuthToken());
    }

    @Test
    void authenticateWithBadCredentialsExceptionTest() {
        // Arrange
        UserDto testDto = GenerateTestData.generateUserDto();
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        // Act + Assert
        assertThrows(BadCredentialsException.class, () -> controllerUnderTest.login(testDto));
    }

}
