package net.franzka.kams.authentication.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import io.jsonwebtoken.MalformedJwtException;

import java.util.List;

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
    protected ResponseEntity<Object> handleWrongActivationTokenException(WrongActivationTokenException ex,
                                                                         WebRequest request) {
        log.error("Wrong Activation Token");
        return handleExceptionInternal(ex, wrongActivationTokenErrorMessage, new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);

    }

    /**
     * Handle constraint violations
     * @param ex Exception contains error messages
     * @param headers
     * @param status
     * @param request
     * @return  List<String> containing error messages relative with entity validity constraint violations
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        log.error("handleMethodArgumentNotValid " + ex.getBindingResult().getFieldErrors());
        List<FieldError> errors = ex.getBindingResult().getFieldErrors();
        List<String> errorMessages = errors.stream().map(err -> err.getDefaultMessage()).toList();
        if (errorMessages.size() == 1) {
            return handleExceptionInternal(ex, errorMessages.get(0), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
        }
        else {
            return handleExceptionInternal(ex, errorMessages, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
        }
    }

    @Value("${net.franzka.kams.authentication.error.bad-credentials}")
    private String badCredentialsErrorMessage;

    /**
     * BadCredentialsException
     * @param ex : {@link BadCredentialsException}
     * @param request : {@link WebRequest}
     * @return Http Status 401 and the related error message stored in messages.properties
     */
    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex,
                                                                          WebRequest request) {
        log.error("Wrong Activation Token");
        return handleExceptionInternal(ex, badCredentialsErrorMessage, new HttpHeaders(),
                HttpStatus.UNAUTHORIZED, request);

    }

    @Value("${net.franzka.kams.authentication.error.wrong-auth-token}")
    private String wrongAuthTokenErrorMessage;

    @ExceptionHandler(MalformedJwtException.class)
    protected ResponseEntity<Object> handleMalformedJwtException(MalformedJwtException ex,
                                                                   WebRequest request) {
        log.error("Wrong AuthToken : MalformedJwtException");
        return handleExceptionInternal(ex, wrongAuthTokenErrorMessage, new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);

    }

    @ExceptionHandler(SignatureException.class)
    protected ResponseEntity<Object> handleSignatureException(SignatureException ex,
                                                                 WebRequest request) {
        log.error("Wrong AuthToken : SignatureException");
        return handleExceptionInternal(ex, wrongAuthTokenErrorMessage, new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);

    }



    @Value("${net.franzka.kams.authentication.error.auth-token-expired}")
    private String authTokenExpiredErrorMessage;

    @ExceptionHandler(ExpiredJwtException.class)
    protected ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex,
                                                                 WebRequest request) {
        log.error("AuthToken Expired");
        return handleExceptionInternal(ex, authTokenExpiredErrorMessage, new HttpHeaders(),
                HttpStatus.UNAUTHORIZED, request);

    }




}
