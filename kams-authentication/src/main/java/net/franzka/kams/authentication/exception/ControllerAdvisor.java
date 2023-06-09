package net.franzka.kams.authentication.exception;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.sql.SQLException;

@ControllerAdvice
@Log4j2
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @Value("${error.bad-credentials}")
    private String badCredentials;

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex,
                                                                    WebRequest request) {
        return handleExceptionInternal(ex, badCredentials, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @Value("${error.invalid-access}")
    private String invalidAccess;

    @ExceptionHandler(InvalidAccessException.class)
    protected ResponseEntity<Object> handleInvalidAccessException(InvalidAccessException ex,
                                                                   WebRequest request) {
        return handleExceptionInternal(ex, invalidAccess, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @Value("${error.user-already-exists}")
    private String userAlreadyExists;

    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex,
                                                                  WebRequest request) {
        return handleExceptionInternal(ex, userAlreadyExists, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Value("${error.token-expired}")
    private String expiredToken;

    @ExceptionHandler(ExpiredTokenException.class)
    protected ResponseEntity<Object> handleExpiredTokenException(ExpiredTokenException ex, WebRequest request) {
        return handleExceptionInternal(ex, expiredToken, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(SQLException.class)
    protected ResponseEntity<Object> handleSQLException(SQLException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE, request);
    }

}
