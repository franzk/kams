package net.franzka.kams.email.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.RandomString;
import net.franzka.kams.email.dto.EmailDto;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("integrationTest")
class MailSenderControllerTestIT {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void sendEmailTestIT() throws Exception {

        EmailDto testEmailDto = new EmailDto();
        testEmailDto.setToAddress("!! TESTING EMAIL HERE !!");
        testEmailDto.setSubject("Subject for testing");
        testEmailDto.setContent(RandomString.make(256));

        String body = mapper.writeValueAsString(testEmailDto);

        mockMvc.perform(post("/send").contentType(APPLICATION_JSON_UTF8).content(body))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void sendEmailWithWrongToAddressTestIT() throws Exception {

        EmailDto testEmailDto = new EmailDto();
        testEmailDto.setToAddress("wrongtoaddress");
        testEmailDto.setSubject("Subject for testing");
        testEmailDto.setContent(RandomString.make(256));

        String body = mapper.writeValueAsString(testEmailDto);

        mockMvc.perform(post("/send").contentType(APPLICATION_JSON_UTF8).content(body))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }


}
