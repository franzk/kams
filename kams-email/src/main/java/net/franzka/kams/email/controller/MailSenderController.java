package net.franzka.kams.email.controller;

import lombok.extern.log4j.Log4j2;
import net.franzka.kams.email.dto.EmailDto;
import net.franzka.kams.email.exception.MailSenderException;
import net.franzka.kams.email.service.MailSenderService;
import net.franzka.kams.email.service.impl.MailSenderServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class MailSenderController {

    private MailSenderService mailSenderService;

    public MailSenderController(MailSenderServiceImpl mailSenderServiceImpl) {
        this.mailSenderService = mailSenderServiceImpl;
    }

    public void setMailSenderService(MailSenderService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }


    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailDto emailDto) throws MailSenderException {
        mailSenderService.sendEmail(emailDto);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

}
