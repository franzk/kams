package net.franzka.kams.authentication.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@PropertySource("classpath:messages.properties")
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @Value("${error.activation-link-expired}")
    private String activationTokenExpiredErrorMessage;

    @ExceptionHandler(ActivationTokenExpiredException.class)
    protected ResponseEntity<Object> handleActivationTokenExpiredException(ActivationTokenExpiredException ex,
                                                                           WebRequest request) {

        return handleExceptionInternal(ex, activationTokenExpiredErrorMessage, new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);

    }

    @Value("${error.user-already-activated}")
    private String userAlreadyActivatedErrorMessage;

    @ExceptionHandler(UserAlreadyActivatedException.class)
    protected ResponseEntity<Object> handleUserAlreadyActivatedException(UserAlreadyActivatedException ex,
                                                                           WebRequest request) {

        return handleExceptionInternal(ex, userAlreadyActivatedErrorMessage, new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);

    }

    @Value("${error.user-already-exists}")
    private String userAlreadyExistsErrorMessage;

    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex,
                                                                         WebRequest request) {

        return handleExceptionInternal(ex, userAlreadyExistsErrorMessage, new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);

    }

    @Value("${error.wrong-activation-token}")
    private String wrongActivationTokenErrorMessage;

    @ExceptionHandler(WrongActivationTokenException.class)
    protected ResponseEntity<Object> handleUWrongActivationTokenException(WrongActivationTokenException ex,
                                                                      WebRequest request) {

        return handleExceptionInternal(ex, wrongActivationTokenErrorMessage, new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);

    }

}
