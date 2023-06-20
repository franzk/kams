package net.franzka.kams.authentication.service;

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
import net.franzka.kams.authentication.service.interfaces.RegistrationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

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

    public String register(UserDto userDto) throws UserAlreadyExistsException {

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) throw new UserAlreadyExistsException();

        // save the new unverified user
        unverifiedUserRepository.save(makeNewUnverifiedUser(userDto));

        return "OK";

    }

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

    @Value("${activationtoken.expiration.minutes}")
    private int validationTokenDuration;

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
