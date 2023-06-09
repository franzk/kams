package net.franzka.kams.authentication.service;

import net.bytebuddy.utility.RandomString;
import net.franzka.kams.authentication.model.User;
import net.franzka.kams.authentication.repository.UserRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
class JpaUserDetailsServiceTest {

    @InjectMocks
    private JpaUserDetailsService serviceUnderTest;

    @Mock
    private UserRepository userRepository;

    @Test
    void loadUserByUsernameTest() {
        // Arrange
        User testUser = new User();
        testUser.setEmail(RandomString.make(64));
        testUser.setPassword(RandomString.make(64));
        testUser.setRole(RandomString.make(64));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(testUser));

        // Act
        UserDetails result = serviceUnderTest.loadUserByUsername(testUser.getEmail());

        // Assert
        assertThat(result).isNotNull();
    }

    @Test
    void loadUserByUsernameWithUsernameNotFoundExceptionTest() {
        // Arrange
        String testUsername = RandomString.make(64);
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        // Act + Assert
        assertThrows(UsernameNotFoundException.class,
                () -> serviceUnderTest.loadUserByUsername(testUsername));

    }


}
