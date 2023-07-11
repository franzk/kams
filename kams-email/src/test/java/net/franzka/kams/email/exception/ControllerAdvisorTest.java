package net.franzka.kams.email.exception;

import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
public class ControllerAdvisorTest {

    @InjectMocks
    ControllerAdvisor classUnderTest;

    @Test
    void handleMailSenderExceptionTest() {
        // Arrange
        String exceptionMessage = RandomString.make(64);
        WebRequest request = mock(WebRequest.class);

        // Act
        ResponseEntity<Object> result =  classUnderTest.handleMailSenderException(new MailSenderException(exceptionMessage), request);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

    }


}
