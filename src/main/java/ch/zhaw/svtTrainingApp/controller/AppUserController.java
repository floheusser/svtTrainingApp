package ch.zhaw.svtTrainingApp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.svtTrainingApp.model.AppUser;
import ch.zhaw.svtTrainingApp.model.dto.UserUpdateDTO;
import ch.zhaw.svtTrainingApp.repository.AppUserRepository;

@RestController
@RequestMapping("/api")
public class AppUserController {

    @Autowired
    AppUserRepository userRepository;

    @GetMapping("/user/account")
    public ResponseEntity<AppUser> getUserByEmail(@AuthenticationPrincipal Jwt jwt) {
        Optional<AppUser> optUser = userRepository.findByEmail(jwt.getClaimAsString("email"));
        if (optUser.isPresent()) {
            return new ResponseEntity<>(optUser.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/user/update")
    public ResponseEntity<Object> updateUser(@AuthenticationPrincipal Jwt jwt, @RequestBody UserUpdateDTO uDTO) {
        Optional<AppUser> optUser = userRepository.findByEmail(jwt.getClaimAsString("email"));
        if (optUser.isPresent()) {
            AppUser uDAO = optUser.get();
            uDAO.setName(uDTO.getName());
            uDAO.setType(uDTO.getType());
            AppUser user = userRepository.save(uDAO);
            return new ResponseEntity<>(user, HttpStatus.OK); 
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
    }

    @GetMapping("/user/trainers")
    public ResponseEntity<List<AppUser>> getAllTrainers() {
        List<AppUser> allTrainers = userRepository.findByType("trainer");
        return new ResponseEntity<>(allTrainers, HttpStatus.OK);
    }

    @GetMapping("/user/helptrainers")
    public ResponseEntity<List<AppUser>> getAllHelpTrainers() {
        List<AppUser> allHelpTrainers = userRepository.findByType("helptrainer");
        return new ResponseEntity<>(allHelpTrainers, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<AppUser>> getAllUsers() {
        List<AppUser> allUsers = userRepository.findAll();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }
}
