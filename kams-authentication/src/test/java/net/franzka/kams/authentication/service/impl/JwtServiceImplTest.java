package net.franzka.kams.authentication.service.impl;

import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;

import static org.assertj.core.api.Assertions.assertThat;

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


}
