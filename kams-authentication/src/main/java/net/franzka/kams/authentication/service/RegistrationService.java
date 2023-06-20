package net.franzka.kams.authentication.service.interfaces;

import net.franzka.kams.authentication.dto.UserDto;
import net.franzka.kams.authentication.exception.ActivationTokenExpiredException;
import net.franzka.kams.authentication.exception.UserAlreadyActivatedException;
import net.franzka.kams.authentication.exception.UserAlreadyExistsException;
import net.franzka.kams.authentication.exception.WrongActivationTokenException;
import net.franzka.kams.authentication.model.User;

public interface RegistrationService {

    String register(UserDto userDto) throws UserAlreadyExistsException;

    User activate(String activationToken) throws WrongActivationTokenException, UserAlreadyActivatedException, ActivationTokenExpiredException;

}
