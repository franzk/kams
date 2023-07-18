package net.franzka.kams.authentication.service.impl;

import lombok.extern.log4j.Log4j2;
import net.franzka.kams.authentication.dto.EmailDto;
import net.franzka.kams.authentication.exception.SendMailException;
import net.franzka.kams.authentication.service.SendMailService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Log4j2
public class SendMailServiceImpl implements SendMailService {

    public void sendMail(String toAddress, String subject, String content) {

        EmailDto emailDto = new EmailDto();
        emailDto.setToAddress(toAddress);
        emailDto.setSubject(subject);
        emailDto.setContent(content);


        WebClient.create("http://localhost:8091")
                .post().uri("/send")
                .bodyValue(emailDto)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(
                        HttpStatus.INTERNAL_SERVER_ERROR::equals,
                        response -> response.bodyToMono(String.class).map(SendMailException::new))
                .bodyToMono(String.class)
                .subscribe(log::info, log::error);

    }

}
