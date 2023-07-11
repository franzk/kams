package net.franzka.kams.email.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import net.franzka.kams.email.dto.EmailDto;
import net.franzka.kams.email.exception.MailSenderException;
import net.franzka.kams.email.service.MailSenderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

@Service
@Log4j2
public class MailSenderServiceImpl implements MailSenderService {

    private final JavaMailSender mailSender;

    @Value("${net.franzka.kams.email-address}")
    private String fromAddress;

    @Value("${net.franzka.kams.sender-name}")
    private String senderName;

    public MailSenderServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(EmailDto emailDto) throws MailSenderException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(fromAddress, senderName);
            helper.setTo(emailDto.getToAddress());
            helper.setSubject(emailDto.getSubject());
            helper.setText(emailDto.getContent(), true);
        }
        catch(Exception ex) {
            throw new MailSenderException(ex.getMessage());
        }

        try {
            mailSender.send(message);
        }
        catch(MailSendException ex) {
            String aa = Arrays.toString(ex.getMessageExceptions());
            throw new MailSenderException(aa);
        }




        log.info("Email \"" + emailDto.getSubject() + "\" envoyé à " + emailDto.getToAddress());

    }


}
