package net.franzka.kams.email.config;

import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
public class MailSenderConfigTest {

    @InjectMocks
    MailSenderConfig classUnderTest;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(classUnderTest, "host", RandomString.make(64));
        ReflectionTestUtils.setField(classUnderTest, "port", 123);
        ReflectionTestUtils.setField(classUnderTest, "username", RandomString.make(64));
        ReflectionTestUtils.setField(classUnderTest, "password", RandomString.make(64));
        ReflectionTestUtils.setField(classUnderTest, "smtpAuth", RandomString.make(64));
        ReflectionTestUtils.setField(classUnderTest, "smtpSSLEnable", RandomString.make(64));
    }

    @Test
    void javaMailSenderTest() {
        JavaMailSender result = classUnderTest.javaMailSender();
        assertThat(result).isNotNull();
    }

}
