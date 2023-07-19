package net.franzka.kams.profile.controller;

import lombok.extern.log4j.Log4j2;
import net.franzka.kams.profile.dto.ProfileDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@Log4j2
public class ProfileController {

    @GetMapping("/")
    public ResponseEntity<ProfileDto> test(@RequestHeader("loggedInUserEmail") String loggedInUserEmail) {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setEmail(loggedInUserEmail);
        return new ResponseEntity<>(profileDto, HttpStatus.OK);
    }


    @GetMapping("/show")
    public ResponseEntity<ProfileDto> show(@RequestParam String email) {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setEmail(email);
        return new ResponseEntity<>(profileDto, HttpStatus.OK);
    }


}
