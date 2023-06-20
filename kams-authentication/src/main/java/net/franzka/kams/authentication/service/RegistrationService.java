package net.franzka.kams.authentication.service;

import net.franzka.kams.authentication.dto.UserDto;
import net.franzka.kams.authentication.exception.ActivationTokenExpiredException;
import net.franzka.kams.authentication.exception.UserAlreadyActivatedException;
import net.franzka.kams.authentication.exception.UserAlreadyExistsException;
import net.franzka.kams.authentication.exception.WrongActivationTokenException;
import net.franzka.kams.authentication.model.UnverifiedUser;
import net.franzka.kams.authentication.model.User;

/**
 * interface for RegistrationService
 */
public interface RegistrationService {

    /**
     * Register a new user
     * @param userDto : contains new user's data
     * @return new {@link UnverifiedUser} data mapped in a {@link UserDto} object
     * @throws {@link UserAlreadyExistsException}
     */
    UserDto register(UserDto userDto) throws UserAlreadyExistsException;

    /**
     * Activate an {@link UnverifiedUser}
     * @param activationToken : the activation token of the {@link UnverifiedUser}
     * @return the created {@link User}
     * @throws {@link WrongActivationTokenException}
     * @throws {@link UserAlreadyActivatedException}
     * @throws {@link ActivationTokenExpiredException}
     */
    User activate(String activationToken) throws WrongActivationTokenException, UserAlreadyActivatedException, ActivationTokenExpiredException;

}
