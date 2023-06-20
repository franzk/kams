package net.franzka.kams.authentication.controller;

import net.franzka.kams.authentication.dto.UserDto;
import net.franzka.kams.authentication.dto.UserMapper;
import net.franzka.kams.authentication.exception.ActivationTokenExpiredException;
import net.franzka.kams.authentication.exception.UserAlreadyActivatedException;
import net.franzka.kams.authentication.exception.UserAlreadyExistsException;
import net.franzka.kams.authentication.exception.WrongActivationTokenException;
import net.franzka.kams.authentication.model.UnverifiedUser;
import net.franzka.kams.authentication.model.User;
import net.franzka.kams.authentication.repository.UnverifiedUserRepository;
import net.franzka.kams.authentication.service.RegistrationService;
import net.franzka.kams.authentication.service.impl.RegistrationServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * new user registration API
 */
@RestController
public class RegistrationController {
    private RegistrationService registrationService;

    public RegistrationController(RegistrationServiceImpl registrationServiceImpl) {
        this.registrationService = registrationServiceImpl;
    }

    public void setRegistrationService(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }


    /**
     * POST request : Handle new user registration.
     * Create a new Unverified User with a random activation token and encrypt the given password
     * @param {@link UserDto} : Request Body in Json Format
     * @return Http Status 201 and the email of the Unverified User created
     * @throws {@link UserAlreadyExistsException}
     */
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto) throws UserAlreadyExistsException {
        return new ResponseEntity<>(registrationService.register(userDto), HttpStatus.CREATED);
    }

    /**
     * GET Request : activate unverified user.
     * Copy the unverified user infos corresponding to the activationToken to a new user and then delete the unverified user
     * @param activationToken
     * @return Http Status 201 and a {@link UserDto} containing new user infos
     * @throws {@link ActivationTokenExpiredException}
     * @throws {@link WrongActivationTokenException}
     * @throws {@link UserAlreadyActivatedException}
     */
    @GetMapping("/activate")
    public ResponseEntity<UserDto> activate(@RequestParam String activationToken) throws ActivationTokenExpiredException, WrongActivationTokenException, UserAlreadyActivatedException {
        User userResult = registrationService.activate(activationToken);
        return new ResponseEntity<>(UserMapper.userToUserDto(userResult), HttpStatus.CREATED);
    }

}
