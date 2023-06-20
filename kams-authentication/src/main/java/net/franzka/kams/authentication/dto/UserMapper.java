package net.franzka.kams.authentication.dto;

import net.franzka.kams.authentication.model.User;

/**
 * Static Methods that map {@link User} to {@link UserDto} and {@link UserDto} to {@link User}
 */
public class UserMapper {

    private UserMapper() {

    }

    /**
     * Map {@link User} to {@link UserDto}
     * @param user : {@link User} to map
     * @return mapped {@link UserDto}
     */
    public static UserDto userToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        // never communicate the password !
        userDto.setRole(user.getRole());
        return userDto;
    }

    /**
     * Map {@link UserDto} to {@link User}
     * @param userDto : {@link UserDto} to map
     * @return mapped {@link User}
     */
    public static User userDtoToUser(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());
        return user;
    }

}
