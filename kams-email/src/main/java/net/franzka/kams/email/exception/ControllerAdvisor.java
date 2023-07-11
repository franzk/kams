package net.franzka.kams.email.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Log4j2
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    /**
     * handle MailSenderException
     * @param ex : {@link MailSenderException}
     * @param request : {@link WebRequest}
     * @return Http Status 401 and the related error message stored in messages.properties
     */
    @ExceptionHandler(MailSenderException.class)
    protected ResponseEntity<Object> handleMailSenderException(MailSenderException ex,
                                                                   WebRequest request) {
        log.error("Mail Sender Error : " + ex.getMessage());
        return handleExceptionInternal(ex, "Mail Sender Error : " + ex.getMessage() , new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
