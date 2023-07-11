package net.franzka.kams.email.service;

import net.franzka.kams.email.dto.EmailDto;
import net.franzka.kams.email.exception.MailSenderException;

public interface MailSenderService {

    void sendEmail(EmailDto emailDto) throws MailSenderException;

}
