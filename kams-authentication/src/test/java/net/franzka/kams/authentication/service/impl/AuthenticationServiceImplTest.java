package net.franzka.kams.authentication.service.impl;

import net.bytebuddy.utility.RandomString;
import net.franzka.kams.authentication.dto.AuthTokenDto;
import net.franzka.kams.authentication.service.JwtService;
import net.franzka.kams.authentication.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
class AuthenticationServiceImplTest {

    @InjectMocks
    AuthenticationServiceImpl serviceUnderTest;

    @Mock
    JwtService jwtService;

    @BeforeEach
    private void setup() {
        serviceUnderTest.setJwtService(jwtService);
    }

    @Test
    void generateTokenTest() {
        // Arrange
        String testEmail = RandomString.make(64);
        String testToken = RandomString.make(64);
        when(jwtService.generateToken(testEmail)).thenReturn(testToken);

        // Act
        AuthTokenDto result = serviceUnderTest.generateToken(testEmail);

        // Assert
        assertThat(result.getAuthToken()).isEqualTo(testToken);
    }

}
