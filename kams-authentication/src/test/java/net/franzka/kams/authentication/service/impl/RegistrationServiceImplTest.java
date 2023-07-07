package net.franzka.kams.authentication.service.impl;

import net.bytebuddy.utility.RandomString;
import net.franzka.kams.authentication.dto.UserDto;
import net.franzka.kams.authentication.exception.ActivationTokenExpiredException;
import net.franzka.kams.authentication.exception.UserAlreadyActivatedException;
import net.franzka.kams.authentication.exception.UserAlreadyExistsException;
import net.franzka.kams.authentication.exception.WrongActivationTokenException;
import net.franzka.kams.authentication.model.UnverifiedUser;
import net.franzka.kams.authentication.model.User;
import net.franzka.kams.authentication.repository.UnverifiedUserRepository;
import net.franzka.kams.authentication.repository.UserRepository;
import net.franzka.kams.authentication.utils.GenerateTestData;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
class RegistrationServiceImplTest {

    @InjectMocks
    private RegistrationServiceImpl serviceUnderTest;

    @Mock
    private UnverifiedUserRepository unverifiedUserRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void registerTest() throws UserAlreadyExistsException {
        // Arrange
        UserDto testDto = GenerateTestData.generateUserDto();
        UnverifiedUser testUnverifiedUser = GenerateTestData.generateUnverifiedUser();
        when(unverifiedUserRepository.save(any())).thenReturn(testUnverifiedUser);

        //Act
        UserDto result = serviceUnderTest.register(testDto);

        // Assert
        assertThat(result).isNotNull();
        verify(userRepository, times(1)).findByEmail(any());
        verify(unverifiedUserRepository, times(1)).deleteByEmail(any());
        verify(unverifiedUserRepository, times(1)).save(any());


    }

    @Test
    void registerWithUserAlreadyExistExceptionTest() {
        // Arrange
        UserDto testDto = GenerateTestData.generateUserDto();
        User testUser = GenerateTestData.generateUser();
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(testUser));

        // Act + Assert
        assertThrows(UserAlreadyExistsException.class, ()-> serviceUnderTest.register(testDto));
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

    @Test
    void activateWithWrongActivationTokenExceptionTest() {

        String testActivationToken = RandomString.make(64);
        when(unverifiedUserRepository.findByActivationToken(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(WrongActivationTokenException.class, ()-> serviceUnderTest.activate(testActivationToken));

    }

    @Test
    void activateWithUserAlreadyActivatedExceptionTest() {

        String testActivationToken = RandomString.make(64);
        when(unverifiedUserRepository.findByActivationToken(any()))
                .thenReturn(Optional.of(GenerateTestData.generateUnverifiedUser()));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(GenerateTestData.generateUser()));

        // Act + Assert
        assertThrows(UserAlreadyActivatedException.class, ()-> serviceUnderTest.activate(testActivationToken));

    }

    @Test
    void activateWithActivationTokenExpiredExceptionTest() {

        String testActivationToken = RandomString.make(64);
        UnverifiedUser testUnverifiedUser = GenerateTestData.generateUnverifiedUser();
        testUnverifiedUser.setCreationTime(LocalDateTime.of(2000, 1, 1, 0, 0));
        when(unverifiedUserRepository.findByActivationToken(any())).thenReturn(Optional.of(testUnverifiedUser));
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(ActivationTokenExpiredException.class, ()-> serviceUnderTest.activate(testActivationToken));

    }

}
