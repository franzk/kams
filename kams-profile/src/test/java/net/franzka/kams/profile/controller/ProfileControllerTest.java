package net.franzka.kams.profile.controller;

import net.bytebuddy.utility.RandomString;
import net.franzka.kams.profile.dto.ProfileDto;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
public class ProfileControllerTest {

    @InjectMocks
    ProfileController controllerUnderTest;

    @Test
    void showTest() {
        // Arrange
        String testEmail = RandomString.make(64);

        // Act
        ResponseEntity<ProfileDto> result = controllerUnderTest.show(testEmail);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }




}
