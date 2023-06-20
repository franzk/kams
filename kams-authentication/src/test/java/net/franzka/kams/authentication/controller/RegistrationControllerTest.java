package net.franzka.kams.authentication.controller;

import net.bytebuddy.utility.RandomString;
import net.franzka.kams.authentication.dto.UserMapper;
import net.franzka.kams.authentication.exception.UserAlreadyActivatedException;
import net.franzka.kams.authentication.exception.ActivationTokenExpiredException;
import net.franzka.kams.authentication.exception.WrongActivationTokenException;
import net.franzka.kams.authentication.model.User;
import net.franzka.kams.authentication.service.interfaces.RegistrationService;
import net.franzka.kams.authentication.utils.GenerateTestData;
import net.franzka.kams.authentication.dto.UserDto;
import net.franzka.kams.authentication.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
class RegistrationControllerTest {

    @InjectMocks
    private RegistrationController controllerUnderTest;

    @Mock
    private RegistrationService registrationService;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    public void setup() {
        controllerUnderTest.setRegistrationService(registrationService);
    }

    @Test
    void registerTest() throws UserAlreadyExistsException {
        // Arrange
        UserDto testDto = GenerateTestData.generateUserDto();
        when(registrationService.register(any())).thenReturn("OK");

        // Act
        ResponseEntity<String> result = controllerUnderTest.register(testDto);

        // Assert
        verify(registrationService, times(1)).register(testDto);
        assertThat(result.getBody()).isEqualTo("OK");

    }

    @Test
    void activateTest() throws ActivationTokenExpiredException, WrongActivationTokenException, UserAlreadyActivatedException {
        // Arrange
        String testToken = RandomString.make(64);
        User resultUser = GenerateTestData.generateUser();
        when(registrationService.activate(testToken)).thenReturn(resultUser);

        // Act
        ResponseEntity<User> result = controllerUnderTest.activate(testToken);

        // Assert
        verify(registrationService, times(1)).activate(testToken);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody()).isEqualTo(resultUser);

    }




}
