package net.franzka.kams.authentication.service;

import net.franzka.kams.authentication.exception.SendMailException;

public interface SendMailService {

    void sendMail(String toAddress, String subject, String content);

}
