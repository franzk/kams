package net.franzka.kams.authentication.utils;

import net.bytebuddy.utility.RandomString;
import net.franzka.kams.authentication.dto.UserDto;
import net.franzka.kams.authentication.model.UnverifiedUser;
import net.franzka.kams.authentication.model.User;

import java.time.LocalDateTime;

public class GenerateTestData {

    public static User generateUser() {
        User testUser = new User();
        testUser.setEmail(generateTestEmail());
        testUser.setPassword("Aa+1" + RandomString.make(64));
        testUser.setRole(RandomString.make(64));
        return testUser;
    }

    public static UserDto generateUserDto() {
        UserDto testDto = new UserDto();
        testDto.setEmail(generateTestEmail());
        testDto.setPassword("Aa+1" + RandomString.make(64));
        testDto.setRole(RandomString.make(64));
        return testDto;
    }

    public static UnverifiedUser generateUnverifiedUser() {
        UnverifiedUser testUnverifiedUser = new UnverifiedUser();
        testUnverifiedUser.setEmail(generateTestEmail());
        testUnverifiedUser.setPassword("Aa+1" + RandomString.make(64));
        testUnverifiedUser.setRole(RandomString.make(64));
        testUnverifiedUser.setActivationToken(RandomString.make(64));
        testUnverifiedUser.setCreationTime(LocalDateTime.now());
        return testUnverifiedUser;
    }

    public static String generateTestEmail() {
        return RandomString.make(20) + "@" + RandomString.make(20) + "." + RandomString.make(3);
    }


}
