package net.franzka.kams.email.controller;

import net.franzka.kams.email.service.impl.MailSenderServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MailSenderControllerTest {

    @InjectMocks
    private MailSenderController mailSenderController;

    @Mock
    MailSenderServiceImpl mailSenderService;




}
