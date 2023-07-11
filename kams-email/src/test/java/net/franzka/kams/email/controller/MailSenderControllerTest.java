package net.franzka.kams.email.controller;

import net.bytebuddy.utility.RandomString;
import net.franzka.kams.email.dto.EmailDto;
import net.franzka.kams.email.exception.MailSenderException;
import net.franzka.kams.email.service.MailSenderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
class MailSenderControllerTest {

    @InjectMocks
    private MailSenderController controllerUnderTest;

    @Mock
    MailSenderService mailSenderService;

    @BeforeEach
    void setup() {
        controllerUnderTest.setMailSenderService(mailSenderService);
    }

    @Test
    void sendEmailTest() throws MailSenderException {
        // Arrange
        EmailDto emailDto = new EmailDto();
        emailDto.setToAddress(RandomString.make(64));
        emailDto.setSubject(RandomString.make(64));
        emailDto.setContent(RandomString.make(256));

        // Act
        controllerUnderTest.sendEmail(emailDto);

        // Assert
        verify(mailSenderService, times(1)).sendEmail(emailDto);

    }

    @Test
    void sendEmailWithMailSenderExceptionTest() throws MailSenderException {
        // Arrange
        EmailDto emailDto = new EmailDto();
        doThrow(new MailSenderException("")).when(mailSenderService).sendEmail(emailDto);

        // Act + Assert
        assertThrows(MailSenderException.class, () -> controllerUnderTest.sendEmail(emailDto));


    }



}
