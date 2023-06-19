package net.franzka.kams.authentication.service;

import net.franzka.kams.authentication.exception.UserAlreadyActivatedException;
import net.franzka.kams.authentication.exception.ActivationTokenExpiredException;
import net.franzka.kams.authentication.exception.WrongActivationTokenException;
import net.franzka.kams.authentication.model.UnverifiedUser;
import net.franzka.kams.authentication.model.User;
import net.franzka.kams.authentication.repository.UnverifiedUserRepository;
import net.franzka.kams.authentication.utils.GenerateTestData;
import net.franzka.kams.authentication.dto.UserDto;
import net.franzka.kams.authentication.exception.UserAlreadyExistException;
import net.franzka.kams.authentication.repository.UserRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
class RegistrationServiceTest {

    @InjectMocks
    private RegistrationService serviceUnderTest;

    @Mock
    private UnverifiedUserRepository unverifiedUserRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void registerTest() throws UserAlreadyExistException {

        // Arrange
        UserDto testDto = GenerateTestData.generateUserDto();

        //Act
        serviceUnderTest.register(testDto);

        // Assert
        verify(unverifiedUserRepository, times(1)).save(any());

    }

    @Test
    void activateTest() throws WrongActivationTokenException, UserAlreadyActivatedException, ActivationTokenExpiredException {

        // Arrange
        UnverifiedUser testUnverifiedUser = GenerateTestData.generateUnverifiedUser();
        when(unverifiedUserRepository.findByActivationToken(any())).thenReturn(Optional.of(testUnverifiedUser));

        // Act
        User result = serviceUnderTest.activate(testUnverifiedUser.getActivationToken());

        // Assert
        verify(unverifiedUserRepository, times(1))
                .findByActivationToken(testUnverifiedUser.getActivationToken());
        verify(userRepository, times(1)).save(any());
        verify(unverifiedUserRepository, times(1)).delete(testUnverifiedUser);

        assertThat(result.getEmail()).isEqualTo(testUnverifiedUser.getEmail());
        assertThat(result.getPassword()).isEqualTo(testUnverifiedUser.getPassword());
        assertThat(result.getRole()).isEqualTo(testUnverifiedUser.getRole());



    }


}
