package net.franzka.kams.authentication.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.franzka.kams.authentication.model.UnverifiedUser;
import net.franzka.kams.authentication.repository.UnverifiedUserRepository;
import net.franzka.kams.authentication.utils.GenerateTestData;
import net.franzka.kams.authentication.dto.UserDto;
import net.franzka.kams.authentication.model.User;
import net.franzka.kams.authentication.repository.UserRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Tag("integrationTest")
class RegistrationControllerTestIT {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    UnverifiedUserRepository unverifiedUserRepository;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    @Sql(scripts = "classpath:test.sql")
    void registrationTestIT() throws Exception {
        // Arrange
        UserDto testDto = GenerateTestData.generateUserDto();
        String body = mapper.writeValueAsString(testDto);


        // Act
        ResultActions resultActions = mockMvc
                .perform(post("/register").contentType(APPLICATION_JSON_UTF8).content(body))
                .andDo(print())
                .andExpect(status().isCreated());

        // Assert
        // verify that the unverified user is created
        Optional<UnverifiedUser> optionalNewUnverifiedUser = unverifiedUserRepository.findByEmail(testDto.getEmail());
        assertThat(optionalNewUnverifiedUser).isPresent();
        UnverifiedUser newUnverifiedUser = optionalNewUnverifiedUser.get();
        assertThat(newUnverifiedUser.getPassword()).isEqualTo(testDto.getPassword());
        assertThat(newUnverifiedUser.getRole()).isEqualTo(testDto.getRole());
        assertThat(newUnverifiedUser.getActivationToken()).isNotEmpty();
        assertThat(newUnverifiedUser.getCreationTime()).isNotNull();

        // verify the response of the endpoint
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        assertThat(contentAsString).isEqualTo("OK"); // TODO améliorer


    }

    @Test
    @Sql(scripts = "classpath:test.sql")
    void activateNewUserTest() throws Exception {
        // Arrange

        List<UnverifiedUser> unverifiedUserList = unverifiedUserRepository.findAll();
        UnverifiedUser testUnverifiedUser = unverifiedUserList.get(0);
        String testActivationToken = testUnverifiedUser.getActivationToken();

        // Act
        ResultActions resultActions = mockMvc
                .perform(get("/activate").param("activationToken", testActivationToken))
                .andDo(print())
                .andExpect(status().isCreated());

        // Assert
        // 1- verify that the user is created
        Optional<User> optionalNewUser = userRepository.findByEmail(testUnverifiedUser.getEmail());
        assertThat(optionalNewUser).isPresent();
        User newUser = optionalNewUser.get();
        assertThat(newUser.getPassword()).isEqualTo(testUnverifiedUser.getPassword());
        assertThat(newUser.getRole()).isEqualTo(testUnverifiedUser.getRole());

        // 2- verify the response of the endpoint
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        Map<String, Object> resultData = mapper.readValue(contentAsString, new TypeReference<Map<String, Object>>() {});
        String resultEmail = resultData.get("email").toString();
        String resultPassword = resultData.get("password").toString();
        String resultRole = resultData.get("role").toString();

        assertThat(resultEmail).isEqualTo(testUnverifiedUser.getEmail());
        assertThat(resultPassword).isEqualTo(testUnverifiedUser.getPassword());
        assertThat(resultRole).isEqualTo(testUnverifiedUser.getRole());
    }



}
