package ch.zhaw.svtTrainingApp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.svtTrainingApp.model.AppUser;
import ch.zhaw.svtTrainingApp.repository.AppUserRepository;

@RestController
@RequestMapping("/api")
public class AppUserController {

    @Autowired
    AppUserRepository userRepository;

    @GetMapping("/user/account")
    public ResponseEntity<AppUser> getCustomerByEmail(@AuthenticationPrincipal Jwt jwt) {
        Optional<AppUser> optUser = userRepository.findByEmail(jwt.getClaimAsString("email"));
        if (optUser.isPresent()) {
            return new ResponseEntity<>(optUser.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/users")
    public ResponseEntity<List<AppUser>> getAllCustomers() {
        List<AppUser> allUsers = userRepository.findAll();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }
}
