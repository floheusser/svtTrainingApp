package ch.zhaw.svtTrainingApp.security;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;

import ch.zhaw.svtTrainingApp.model.Customer;
import ch.zhaw.svtTrainingApp.model.Hairdresser;
import ch.zhaw.svtTrainingApp.repository.CustomerRepository;
import ch.zhaw.svtTrainingApp.repository.HairdresserRepository;

class UserValidator implements OAuth2TokenValidator<Jwt> {

    HairdresserRepository hairdresserRepository;
    CustomerRepository customerRepository;
    private final ConcurrentMap<String, Object> locks = new ConcurrentHashMap<>();

    public UserValidator(HairdresserRepository hairdresserRepository, CustomerRepository customerRepository) {
        this.hairdresserRepository = hairdresserRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        OAuth2Error error = new OAuth2Error("invalid_token", "The required email is missing", null);

        String userEmail = jwt.getClaimAsString("email");
        String userRole = jwt.getClaimAsString("user_roles");
        String nickname = jwt.getClaimAsString("nickname");
        synchronized (locks.computeIfAbsent(userEmail, k -> new Object())) {
            if (userEmail != null && !userEmail.equals("")) {
                if (userRole.equals("hairdresser")) {
                    Hairdresser h = hairdresserRepository.findFirstByEmail(userEmail);
                    if (h == null) {
                        hairdresserRepository.save(new Hairdresser(nickname, userEmail));
                    }
                } else if (userRole.equals("customer")) {
                    Customer c = customerRepository.findFirstByEmail(userEmail);
                    if (c == null) {
                        customerRepository.save(new Customer(nickname, userEmail));
                    }
                }
                return OAuth2TokenValidatorResult.success();
            }
        }
        return OAuth2TokenValidatorResult.failure(error);
    }
}
