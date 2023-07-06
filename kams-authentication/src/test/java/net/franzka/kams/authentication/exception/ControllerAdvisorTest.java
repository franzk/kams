package net.franzka.kams.authentication.exception;

import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        ResponseEntity<Object> result =  classUnderTest.handleWrongActivationTokenException(new WrongActivationTokenException(), null);

        // Assert
        assertThat(result.getBody()).isEqualTo(testErrorMessage);
    }

    @Test
    void handleMethodArgumentNotValidTest() {

        // Arrange
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(ex.getBindingResult()).thenReturn(bindingResult);

        String testErrorMessage = RandomString.make(64);
        FieldError testFieldError = new FieldError("antimatter", "antimatter", testErrorMessage);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(testFieldError));

        WebRequest request = mock(WebRequest.class);

        // Act
        ResponseEntity<Object> result =  classUnderTest.handleMethodArgumentNotValid(ex, new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().toString()).contains(testErrorMessage);

    }

    @Test
    void handleMethodArgumentNotValidWithSeveralErrorsTest() {

        // Arrange
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(ex.getBindingResult()).thenReturn(bindingResult);

        String testErrorMessage = RandomString.make(64);
        String testErrorMessage2 = RandomString.make(64);

        FieldError testFieldError = new FieldError("antimatter", "antimatter", testErrorMessage);
        FieldError testFieldError2 = new FieldError("black hole", "black hole", testErrorMessage2);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(testFieldError, testFieldError2));

        WebRequest request = mock(WebRequest.class);

        // Act
        ResponseEntity<Object> result =  classUnderTest.handleMethodArgumentNotValid(ex, new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().toString()).contains(testErrorMessage);
        assertThat(result.getBody().toString()).contains(testErrorMessage2);

    }

}
