package net.franzka.kams.authentication.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.franzka.kams.authentication.controller.RegistrationController;
import net.franzka.kams.authentication.dto.UserDto;
import net.franzka.kams.authentication.model.UnverifiedUser;
import net.franzka.kams.authentication.model.User;
import net.franzka.kams.authentication.repository.UnverifiedUserRepository;
import net.franzka.kams.authentication.repository.UserRepository;
import net.franzka.kams.authentication.utils.GenerateTestData;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.entry;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Tag("integrationTest")
public class AuthenticationControllerTestIT {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UnverifiedUserRepository unverifiedUserRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    RegistrationController registrationController;

    @Test
    @Sql(scripts = "classpath:test.sql")
    void authenticationTestIT() throws Exception {

        // Arrange
        // Create a new user
        UserDto testNewUserDto = GenerateTestData.generateUserDto();
        registrationController.register(testNewUserDto);
        List<UnverifiedUser> unverifiedUserList = unverifiedUserRepository.findByEmail(testNewUserDto.getEmail());
        UnverifiedUser unverifiedUser = unverifiedUserList.get(0);
        registrationController.activate(unverifiedUser.getActivationToken());

        UserDto testUserDto = new UserDto();
        testUserDto.setEmail(testNewUserDto.getEmail());
        testUserDto.setPassword(testNewUserDto.getPassword());

        String body = mapper.writeValueAsString(testUserDto);

        // Act
        ResultActions resultActions = mockMvc
                .perform(post("/auth").contentType(APPLICATION_JSON_UTF8).content(body))
                .andDo(print())
                .andExpect(status().isOk());

        // Assert
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        Map<String, Object> resultData = mapper.readValue(contentAsString, new TypeReference<Map<String, Object>>() {});
        assertThat(resultData)
                .containsKey("authToken")
                .doesNotContain(entry("authToken", ""));

    }

    // TODO test BadCredentials


}
