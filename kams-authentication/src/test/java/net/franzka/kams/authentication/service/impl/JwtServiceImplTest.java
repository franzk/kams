package net.franzka.kams.authentication.service.impl;

import net.franzka.kams.authentication.service.impl.JwtServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import net.bytebuddy.utility.RandomString;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
class JwtServiceImplTest {

    @InjectMocks
    @Spy
    private JwtServiceImpl serviceUnderTest;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(serviceUnderTest, "secret", RandomString.make(64));
    }

    @Test
    void getSignKeyTest() {
        // Act
        Key result = serviceUnderTest.getSignKey();
        // Assert
        assertThat(result).isNotNull();
    }

    @Test
    void generateTokenTest() {
        // Arrange
        String username = RandomString.make(64);

        // Act
        String result = serviceUnderTest.generateToken(username);

        // Assert
        assertThat(result).isNotNull();
    }

//    @Test
//    void validateTokenTest() {
//        // Arrange
//        String username = RandomString.make(64);
//        String token = serviceUnderTest.generateToken(username);
//
//        // Act
//        serviceUnderTest.validateToken(token);
//
//        // Assert
//        verify(serviceUnderTest, times(2)).getSignKey();
//    }
//


}
