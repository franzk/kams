package net.franzka.kams.authentication.dto;

import net.bytebuddy.utility.RandomString;
import net.franzka.kams.authentication.utils.GenerateTestData;
import net.franzka.kams.authentication.model.User;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
class UserMapperTest {


    @Test
    void userToUserDtoTest() {

        // Arrange
        User testUser = new User();
        testUser.setEmail(RandomString.make(64));
        testUser.setPassword(RandomString.make(64));
        testUser.setRole(RandomString.make(64));

        // Act
        UserDto result = UserMapper.userToUserDto(testUser);

        // Assert
        assertThat(result.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(result.getRole()).isEqualTo(testUser.getRole());

    }

    @Test
    void userDtoToUserTest() {

        // Arrange
        UserDto testDto = GenerateTestData.generateUserDto();

        // Act
        User result = UserMapper.userDtoToUser(testDto);

        // Assert
        assertThat(result.getEmail()).isEqualTo(testDto.getEmail());
        assertThat(result.getPassword()).isEqualTo(testDto.getPassword());
        assertThat(result.getRole()).isEqualTo(testDto.getRole());

    }

}
