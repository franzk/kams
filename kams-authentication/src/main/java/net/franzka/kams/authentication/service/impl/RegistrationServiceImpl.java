package net.franzka.kams.authentication.service.impl;

import jakarta.transaction.Transactional;
import net.franzka.kams.authentication.dto.UserDto;
import net.franzka.kams.authentication.exception.ActivationTokenExpiredException;
import net.franzka.kams.authentication.exception.UserAlreadyActivatedException;
import net.franzka.kams.authentication.exception.UserAlreadyExistsException;
import net.franzka.kams.authentication.exception.WrongActivationTokenException;
import net.franzka.kams.authentication.model.UnverifiedUser;
import net.franzka.kams.authentication.model.User;
import net.franzka.kams.authentication.repository.UnverifiedUserRepository;
import net.franzka.kams.authentication.repository.UserRepository;
import net.franzka.kams.authentication.service.RandomString;
import net.franzka.kams.authentication.service.RegistrationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Handle new user registration
 */
@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final UnverifiedUserRepository unverifiedUserRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationServiceImpl(UnverifiedUserRepository unverifiedUserRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.unverifiedUserRepository = unverifiedUserRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * New user registration
     * @param userDto : {@link UserDto} that contains the data of the new user
     * @return {@link UserDto} that contains the data of the unverified user created
     * @throws {@link UserAlreadyExistsException}
     */
    @Transactional
    public UserDto register(UserDto userDto) throws UserAlreadyExistsException {

        // if an active user with this email exists in User table, throw an exception
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) throw new UserAlreadyExistsException();

        // delete the previous inactive registrations with this email in UnverifiedUser table
        unverifiedUserRepository.deleteByEmail(userDto.getEmail());

        // save the new unverified user
        UnverifiedUser unverifiedUser = unverifiedUserRepository.save(makeNewUnverifiedUser(userDto));

        // return a DTO with the new unverified user infos
        UserDto result = new UserDto();
        result.setEmail(unverifiedUser.getEmail());
        result.setRole(unverifiedUser.getRole());
        return result;

    }

    /**
     * Map {@link UserDto} to {@link UnverifiedUser} and encode password
     * @param userDto
     * @return
     */
    protected UnverifiedUser makeNewUnverifiedUser(UserDto userDto) {

        UnverifiedUser newUnverifiedUser = new UnverifiedUser();
        newUnverifiedUser.setEmail(userDto.getEmail());
        newUnverifiedUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if (userDto.getRole() != null) {
            newUnverifiedUser.setRole(userDto.getRole());
        }
        newUnverifiedUser.setActivationToken(RandomString.generateRandomString(64));
        newUnverifiedUser.setCreationTime(LocalDateTime.now());
        return newUnverifiedUser;
    }

    @Value("${net.franzka.kams.authentication.activationtoken-expiration-in-minutes}")
    private int validationTokenDuration;

    /**
     * Activate new User : <br>
     * Copy the {@link UnverifiedUser} found with the activationToken into a new {@link User} and save it. <br>
     * And then delete this {@link UnverifiedUser}
     * @param activationToken : The activation token of the unverified user to activate
     * @return the created {@link User}
     * @throws WrongActivationTokenException
     * @throws UserAlreadyActivatedException
     * @throws ActivationTokenExpiredException
     */
    @Transactional
    public User activate(String activationToken) throws WrongActivationTokenException, UserAlreadyActivatedException, ActivationTokenExpiredException {

        Optional<UnverifiedUser> optionalUnverifiedUser = unverifiedUserRepository.findByActivationToken(activationToken);

        // Wrong Token
        if (optionalUnverifiedUser.isEmpty()) {
            throw new WrongActivationTokenException();
        }

        UnverifiedUser unverifiedUser = optionalUnverifiedUser.get();

        // User already activated
        if (userRepository.findByEmail(unverifiedUser.getEmail()).isPresent()) {
            throw new UserAlreadyActivatedException();
        }

        // Token expired
        if (Duration.between(unverifiedUser.getCreationTime(), LocalDateTime.now()).toMinutes() > validationTokenDuration) {
            throw new ActivationTokenExpiredException();
        }

        // Creation of the new user and deletion of then unverifiedUser
        User newUser = new User();
        newUser.setEmail(unverifiedUser.getEmail());
        newUser.setPassword(unverifiedUser.getPassword());
        newUser.setRole(unverifiedUser.getRole());

        userRepository.save(newUser);

        unverifiedUserRepository.delete(unverifiedUser);

        return newUser;
    }
}
