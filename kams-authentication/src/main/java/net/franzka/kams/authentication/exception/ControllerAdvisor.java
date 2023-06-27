package net.franzka.kams.authentication.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Handle API exceptions
 */
@ControllerAdvice
@PropertySource("classpath:messages.properties")
@Log4j2
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @Value("${net.franzka.kams.authentication.error.activation-link-expired}")
    private String activationTokenExpiredErrorMessage;

    /**
     * ActivationTokenExpiredException
     * @param ex : {@link ActivationTokenExpiredException}
     * @param request : {@link WebRequest}
     * @return Http Status 400 and the related error message stored in messages.properties
     */
    @ExceptionHandler(ActivationTokenExpiredException.class)
    protected ResponseEntity<Object> handleActivationTokenExpiredException(ActivationTokenExpiredException ex,
                                                                           WebRequest request) {
        log.error("Activation Token Expired");
        return handleExceptionInternal(ex, activationTokenExpiredErrorMessage, new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);

    }

    @Value("${net.franzka.kams.authentication.error.user-already-activated}")
    private String userAlreadyActivatedErrorMessage;

    /**
     * UserAlreadyActivatedException
     * @param ex : {@link UserAlreadyActivatedException}
     * @param request : {@link WebRequest}
     * @return Http Status 400 and the related error message stored in messages.properties
     */
    @ExceptionHandler(UserAlreadyActivatedException.class)
    protected ResponseEntity<Object> handleUserAlreadyActivatedException(UserAlreadyActivatedException ex,
                                                                           WebRequest request) {
        log.error("User Already Activated");
        return handleExceptionInternal(ex, userAlreadyActivatedErrorMessage, new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);

    }

    @Value("${net.franzka.kams.authentication.error.user-already-exists}")
    private String userAlreadyExistsErrorMessage;

    /**
     * UserAlreadyExistsException
     * @param ex : {@link UserAlreadyExistsException}
     * @param request : {@link WebRequest}
     * @return Http Status 400 and the related error message stored in messages.properties
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex,
                                                                         WebRequest request) {
        log.error("User Already Exists");
        return handleExceptionInternal(ex, userAlreadyExistsErrorMessage, new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);

    }

    @Value("${net.franzka.kams.authentication.error.wrong-activation-token}")
    private String wrongActivationTokenErrorMessage;

    /**
     * WrongActivationTokenException
     * @param ex : {@link WrongActivationTokenException}
     * @param request : {@link WebRequest}
     * @return Http Status 400 and the related error message stored in messages.properties
     */
    @ExceptionHandler(WrongActivationTokenException.class)
    protected ResponseEntity<Object> handleUWrongActivationTokenException(WrongActivationTokenException ex,
                                                                      WebRequest request) {
        log.error("Wrong Activation Token");
        return handleExceptionInternal(ex, wrongActivationTokenErrorMessage, new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);

    }

}
