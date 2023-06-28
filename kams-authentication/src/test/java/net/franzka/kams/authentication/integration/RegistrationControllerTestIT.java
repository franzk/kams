package net.franzka.kams.authentication.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.RandomString;
import net.franzka.kams.authentication.dto.UserDto;
import net.franzka.kams.authentication.model.UnverifiedUser;
import net.franzka.kams.authentication.model.User;
import net.franzka.kams.authentication.repository.UnverifiedUserRepository;
import net.franzka.kams.authentication.repository.UserRepository;
import net.franzka.kams.authentication.utils.GenerateTestData;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

    @Autowired
    PasswordEncoder passwordEncoder;

    ObjectMapper mapper = new ObjectMapper();


    // Registration endpoint

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
        List<UnverifiedUser> expectedNewUnverifiedUser = unverifiedUserRepository.findByEmail(testDto.getEmail());
        assertThat(expectedNewUnverifiedUser).hasSize(1);
        UnverifiedUser newUnverifiedUser = expectedNewUnverifiedUser.get(0);
        assertThat(newUnverifiedUser.getPassword()).isNotEqualTo(testDto.getPassword()); // not equal due to encryption
        assertThat(newUnverifiedUser.getRole()).isEqualTo(testDto.getRole());
        assertThat(newUnverifiedUser.getActivationToken()).isNotEmpty();
        assertThat(newUnverifiedUser.getCreationTime()).isNotNull();

        // verify the response of the endpoint
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        Map<String, Object> resultData = mapper.readValue(contentAsString, new TypeReference<Map<String, Object>>() {});
        String resultEmail = resultData.get("email").toString();
        String resultRole = resultData.get("role").toString();

        assertThat(resultEmail).isEqualTo(testDto.getEmail());
        assertThat(resultRole).isEqualTo(testDto.getRole());
    }


    @Value("${net.franzka.kams.authentication.error.user-already-exists}")
    private String userAlreadyExistsErrorMessage;

    @Test
    @Sql(scripts = "classpath:test.sql")
    void registrationWithUserAlreadyExistsExceptionTestIT() throws Exception {

        // Arrange
        User testUser = GenerateTestData.generateUser();
        userRepository.save(testUser);
        UserDto testDto = GenerateTestData.generateUserDto();
        testDto.setEmail(testUser.getEmail());
        String body = mapper.writeValueAsString(testDto);

        // Act
        ResultActions resultActions = mockMvc
                .perform(post("/register").contentType(APPLICATION_JSON_UTF8).content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(userAlreadyExistsErrorMessage));
    }


    @Test
    @Sql(scripts = "classpath:test.sql")
    void registrationWithAlreadyExistingUnverifiedUserTestIT() throws Exception {
        // Arrange
        String testEmail = GenerateTestData.generateTestEmail();
        for(int i = 0; i < 10; i++) {
            UnverifiedUser testUnverifiedUser = GenerateTestData.generateUnverifiedUser();
            testUnverifiedUser.setEmail(testEmail);
            unverifiedUserRepository.save(testUnverifiedUser);
        }

        UserDto testDto = GenerateTestData.generateUserDto();
        testDto.setEmail(testEmail);
        String body = mapper.writeValueAsString(testDto);

        // Act
        ResultActions resultActions = mockMvc
                .perform(post("/register").contentType(APPLICATION_JSON_UTF8).content(body))
                .andDo(print())
                .andExpect(status().isCreated());

        // Assert
        List<UnverifiedUser> expectedUnverifiedUser = unverifiedUserRepository.findByEmail(testEmail);
        assertThat(expectedUnverifiedUser).hasSize(1);
    }

    @Value("${net.franzka.kams.authentication.error.invalid-email-format}")
    private String invalidEmailFormatErrorMessage;

    @Test
    @Sql(scripts = "classpath:test.sql")
    void registrationWithInvalidEmailFormatErrorTestIT() throws Exception {

        // Arrange
        UserDto testDto = GenerateTestData.generateUserDto();
        testDto.setEmail("bad email format");
        String body = mapper.writeValueAsString(testDto);

        // Act
        ResultActions resultActions = mockMvc
                .perform(post("/register").contentType(APPLICATION_JSON_UTF8).content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(invalidEmailFormatErrorMessage)));
    }

    @Value("${net.franzka.kams.authentication.error.invalid-password-format}")
    private String invalidPasswordFormatErrorMessage;

    @Test
    @Sql(scripts = "classpath:test.sql")
    void registrationWithInvalidPasswordFormatErrorTestIT() throws Exception {

        // Arrange
        UserDto testDto = GenerateTestData.generateUserDto();
        testDto.setPassword("bad password format");
        String body = mapper.writeValueAsString(testDto);

        // Act
        ResultActions resultActions = mockMvc
                .perform(post("/register").contentType(APPLICATION_JSON_UTF8).content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(invalidPasswordFormatErrorMessage)));
    }

    @Test
    @Sql(scripts = "classpath:test.sql")
    void registrationWithInvalidEmailAndPasswordFormatErrorTestIT() throws Exception {

        // Arrange
        UserDto testDto = GenerateTestData.generateUserDto();
        testDto.setEmail("bad email format");
        testDto.setPassword("bad password format");
        String body = mapper.writeValueAsString(testDto);

        // Act
        ResultActions resultActions = mockMvc
                .perform(post("/register").contentType(APPLICATION_JSON_UTF8).content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(invalidEmailFormatErrorMessage)))
                .andExpect(content().string(containsString(invalidPasswordFormatErrorMessage)));
    }

    // Activation endpoint

    @Test
    @Sql(scripts = "classpath:test.sql")
    void activateNewUserTestIT() throws Exception {
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
        String resultRole = resultData.get("role").toString();

        assertThat(resultEmail).isEqualTo(testUnverifiedUser.getEmail());
        assertThat(resultRole).isEqualTo(testUnverifiedUser.getRole());
    }

    @Value("${net.franzka.kams.authentication.error.activation-link-expired}")
    private String activationTokenExpiredErrorMessage;

    @Test
    @Sql(scripts = "classpath:test.sql")
    void activateNewUserWithActivationTokenExpiredExceptionTestIT() throws Exception {

        // Arrange
        UnverifiedUser unverifiedUser = GenerateTestData.generateUnverifiedUser();
        unverifiedUser.setCreationTime(LocalDateTime.of(2000, 1, 1, 0, 0));
        unverifiedUserRepository.save(unverifiedUser);

        // Act
        mockMvc.perform(get("/activate").param("activationToken", unverifiedUser.getActivationToken()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(activationTokenExpiredErrorMessage));

    }


    @Value("${net.franzka.kams.authentication.error.user-already-activated}")
    private String userAlreadyActivatedErrorMessage;

    @Test
    @Sql(scripts = "classpath:test.sql")
    void activateNewUserWithUserAlreadyActivatedExceptionTestIT() throws Exception {

        // Arrange
        UnverifiedUser unverifiedUser = GenerateTestData.generateUnverifiedUser();
        unverifiedUserRepository.save(unverifiedUser);
        User user = GenerateTestData.generateUser();
        user.setEmail(unverifiedUser.getEmail());
        userRepository.save(user);

        // Act
        mockMvc.perform(get("/activate").param("activationToken", unverifiedUser.getActivationToken()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(userAlreadyActivatedErrorMessage));
    }

    @Value("${net.franzka.kams.authentication.error.wrong-activation-token}")
    private String wrongActivationTokenExceptionErrorMessage;

    @Test
    @Sql(scripts = "classpath:test.sql")
    void activateNewUserWithWrongActivationTokenExceptionTestIT() throws Exception {

        // Arrange
        String wrongActivationToken = RandomString.make(64);

        // Act
        mockMvc.perform(get("/activate").param("activationToken",wrongActivationToken))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(wrongActivationTokenExceptionErrorMessage));

    }





}
