package net.franzka.kams.profile.controller;

import net.franzka.kams.profile.dto.ProfileDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @GetMapping("/show")
    public ResponseEntity<ProfileDto> show(@RequestParam String email) {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setEmail(email);
        return new ResponseEntity<>(profileDto, HttpStatus.OK);
    }


}
