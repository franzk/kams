package net.franzka.kams.authentication.exception;

import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
public class ControllerAdvisorTest {

    @InjectMocks
    private ControllerAdvisor classUnderTest;

    @Test
    void handleActivationTokenExpiredExceptionTest() {
        // Arrange
        String testErrorMessage = RandomString.make(64);
        ReflectionTestUtils.setField(classUnderTest, "activationTokenExpiredErrorMessage", testErrorMessage);

        // Act
        ResponseEntity<Object> result =  classUnderTest.handleActivationTokenExpiredException(new ActivationTokenExpiredException(), null);

        // Assert
        assertThat(result.getBody()).isEqualTo(testErrorMessage);
    }

    @Test
    void handleUserAlreadyActivatedExceptionTest() {
        // Arrange
        String testErrorMessage = RandomString.make(64);
        ReflectionTestUtils.setField(classUnderTest, "userAlreadyActivatedErrorMessage", testErrorMessage);

        // Act
        ResponseEntity<Object> result =  classUnderTest.handleUserAlreadyActivatedException(new UserAlreadyActivatedException(), null);

        // Assert
        assertThat(result.getBody()).isEqualTo(testErrorMessage);
    }

    @Test
    void handleUserAlreadyExistsExceptionTest() {
        // Arrange
        String testErrorMessage = RandomString.make(64);
        ReflectionTestUtils.setField(classUnderTest, "userAlreadyExistsErrorMessage", testErrorMessage);

        // Act
        ResponseEntity<Object> result =  classUnderTest.handleUserAlreadyExistsException(new UserAlreadyExistsException(), null);

        // Assert
        assertThat(result.getBody()).isEqualTo(testErrorMessage);
    }

    @Test
    void handleWrongActivationTokenExceptionTest() {
        // Arrange
        String testErrorMessage = RandomString.make(64);
        ReflectionTestUtils.setField(classUnderTest, "wrongActivationTokenErrorMessage", testErrorMessage);

        // Act
        ResponseEntity<Object> result =  classUnderTest.handleUWrongActivationTokenException(new WrongActivationTokenException(), null);

        // Assert
        assertThat(result.getBody()).isEqualTo(testErrorMessage);
    }

}
