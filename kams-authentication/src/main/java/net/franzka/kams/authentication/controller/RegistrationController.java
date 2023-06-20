package net.franzka.kams.authentication.controller;

import lombok.extern.log4j.Log4j2;
import net.franzka.kams.authentication.dto.UserDto;
import net.franzka.kams.authentication.exception.UserAlreadyActivatedException;
import net.franzka.kams.authentication.exception.UserAlreadyExistsException;
import net.franzka.kams.authentication.exception.ActivationTokenExpiredException;
import net.franzka.kams.authentication.exception.WrongActivationTokenException;
import net.franzka.kams.authentication.model.User;
import net.franzka.kams.authentication.service.RegistrationServiceImpl;
import net.franzka.kams.authentication.service.interfaces.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
public class RegistrationController {
    private RegistrationService registrationService;

    public RegistrationController(RegistrationServiceImpl registrationServiceImpl) {
        this.registrationService = registrationServiceImpl;
    }

    public void setRegistrationService(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto) throws UserAlreadyExistsException {
        String result = registrationService.register(userDto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }


    @GetMapping("/activate")
    public ResponseEntity<User> activate(@RequestParam String activationToken) throws ActivationTokenExpiredException, WrongActivationTokenException, UserAlreadyActivatedException {
        User result = registrationService.activate(activationToken);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

}
