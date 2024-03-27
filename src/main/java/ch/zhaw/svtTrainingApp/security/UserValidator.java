package ch.zhaw.svtTrainingApp.security;

import java.util.List;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import ch.zhaw.svtTrainingApp.model.AppUser;
import ch.zhaw.svtTrainingApp.repository.AppUserRepository;

class UserValidator implements OAuth2TokenValidator<Jwt> {

    private AppUserRepository userRepository;

    public UserValidator(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        OAuth2Error error = new OAuth2Error("invalid_token", "The required email is missing", null);

        String userEmail = jwt.getClaimAsString("email");
        List<String> userRoles = jwt.getClaimAsStringList("user_roles");
        String nickname = jwt.getClaimAsString("nickname");
        if (userEmail != null && !userEmail.equals("")) {
            AppUser user = userRepository.findFirstByEmail(userEmail);
            if (user == null) {
                userRepository.save(new AppUser(nickname, userEmail));
            } else {
                user.setRoles(userRoles);
                userRepository.save(user);
            }
            return OAuth2TokenValidatorResult.success();
        }
        return OAuth2TokenValidatorResult.failure(error);
    }
}
