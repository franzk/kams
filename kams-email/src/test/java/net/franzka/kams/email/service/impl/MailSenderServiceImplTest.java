package net.franzka.kams.email.service.impl;

import jakarta.mail.internet.MimeMessage;
import net.bytebuddy.utility.RandomString;
import net.franzka.kams.email.dto.EmailDto;
import net.franzka.kams.email.exception.MailSenderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
class MailSenderServiceImplTest {

    @InjectMocks
    MailSenderServiceImpl serviceUnderTest;

    @Mock
    JavaMailSender javaMailSender;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(serviceUnderTest, "fromAddress", RandomString.make(64));
        ReflectionTestUtils.setField(serviceUnderTest, "senderName", RandomString.make(64));
    }

    @Test
    void sendEmailTest() throws MailSenderException {
        // Arrange
        EmailDto emailDto = new EmailDto();
        emailDto.setToAddress(RandomString.make(64));
        emailDto.setSubject(RandomString.make(64));
        emailDto.setContent(RandomString.make(256));
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act
        serviceUnderTest.sendEmail(emailDto);

        // Assert
        verify(javaMailSender, times(1)).send(mimeMessage);

    }

    @Test
    void sendEmailWithMailSenderExceptionTest() throws MailSenderException {
        // Arrange
        EmailDto emailDto = new EmailDto();
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act + Assert
        assertThrows(MailSenderException.class, () -> serviceUnderTest.sendEmail(emailDto));

    }

    @Test
    void sendEmailWithMailSenderException2Test() {
        // Arrange
        EmailDto emailDto = new EmailDto();
        emailDto.setToAddress(RandomString.make(64));
        emailDto.setSubject(RandomString.make(64));
        emailDto.setContent(RandomString.make(256));
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new MailSendException("")).when(javaMailSender).send(mimeMessage);

        // Act + Assert
        assertThrows(MailSenderException.class, () -> serviceUnderTest.sendEmail(emailDto));

    }


}
