package net.franzka.kams.authentication.controller;

import net.bytebuddy.utility.RandomString;
import net.franzka.kams.authentication.dto.UserDto;
import net.franzka.kams.authentication.exception.ActivationTokenExpiredException;
import net.franzka.kams.authentication.exception.UserAlreadyActivatedException;
import net.franzka.kams.authentication.exception.UserAlreadyExistsException;
import net.franzka.kams.authentication.exception.WrongActivationTokenException;
import net.franzka.kams.authentication.model.UnverifiedUser;
import net.franzka.kams.authentication.model.User;
import net.franzka.kams.authentication.repository.UnverifiedUserRepository;
import net.franzka.kams.authentication.service.RegistrationService;
import net.franzka.kams.authentication.utils.GenerateTestData;
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

    @BeforeEach
    public void setup() {
        controllerUnderTest.setRegistrationService(registrationService);
    }

    @Test
    void registerTest() throws UserAlreadyExistsException {
        // Arrange
        UserDto testDto = GenerateTestData.generateUserDto();
        UserDto expectedResult = GenerateTestData.generateUserDto();
        when(registrationService.register(any())).thenReturn(expectedResult);

        // Act
        ResponseEntity<UserDto> result = controllerUnderTest.register(testDto);

        // Assert
        verify(registrationService, times(1)).register(testDto);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo(expectedResult.getEmail());
        assertThat(result.getBody().getRole()).isEqualTo(expectedResult.getRole());
    }

    @Test
    void activateTest() throws ActivationTokenExpiredException, WrongActivationTokenException, UserAlreadyActivatedException {
        // Arrange
        String testToken = RandomString.make(64);
        User resultUser = GenerateTestData.generateUser();
        when(registrationService.activate(testToken)).thenReturn(resultUser);

        // Act
        ResponseEntity<UserDto> result = controllerUnderTest.activate(testToken);

        // Assert
        verify(registrationService, times(1)).activate(testToken);
        assertThat(result.getBody()).isNotNull();
        UserDto resultDto = result.getBody();
        assertThat(resultDto.getEmail()).isEqualTo(resultUser.getEmail());
        assertThat(resultDto.getRole()).isEqualTo(resultUser.getRole());

    }




}
