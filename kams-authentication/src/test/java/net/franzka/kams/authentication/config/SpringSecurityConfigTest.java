package net.franzka.kams.authentication.config;

import net.franzka.kams.authentication.service.JpaUserDetailsService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
public class SpringSecurityConfigTest {

    @InjectMocks
    private SpringSecurityConfig classUnderTest;

    @Mock
    HttpSecurity httpSecurity;

    @Test
    void filterChainTest() throws Exception {

        // Arrange
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.userDetailsService(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);

        // Act
        SecurityFilterChain result = classUnderTest.filterChain(httpSecurity);

        // Assert
        verify(httpSecurity, times(1)).build();
    }

    @Test
    void passwordEncoderTest() {
        assertThat(classUnderTest.passwordEncoder()).isNotNull();
    }

    @Test
    void authenticationManagerTest() throws Exception {
        // Arrange
        AuthenticationConfiguration config = mock(AuthenticationConfiguration.class);

        // Act
        classUnderTest.authenticationManager(config);

        // Assert
        verify(config, times(1)).getAuthenticationManager();
    }

}
