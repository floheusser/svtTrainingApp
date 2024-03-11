package ch.zhaw.hairathome.security;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@TestConfiguration
public class TestSecurityConfig {
    // inspired by
    // https://stackoverflow.com/questions/61500578/howto-mock-jwt-authentication-in-a-spring-boot-unit-test
    static final String AUTH0_TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6ImZONC1KeGZ6RUhhV1owekQzV0VRVyJ9.eyJ1c2VyX3JvbGVzIjoiY3VzdG9tZXIiLCJuaWNrbmFtZSI6ImZsbyIsIm5hbWUiOiJmbG9AY3VzdC5jaCIsInBpY3R1cmUiOiJodHRwczovL3MuZ3JhdmF0YXIuY29tL2F2YXRhci84NWFiMzZkNjA3MzBmMzg0ZTQyYzkyYmNhODJjMTMzZj9zPTQ4MCZyPXBnJmQ9aHR0cHMlM0ElMkYlMkZjZG4uYXV0aDAuY29tJTJGYXZhdGFycyUyRmZsLnBuZyIsInVwZGF0ZWRfYXQiOiIyMDIzLTA1LTI4VDEyOjAyOjUzLjc0OFoiLCJlbWFpbCI6ImZsb0BjdXN0LmNoIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJpc3MiOiJodHRwczovL2hhaXJhdGhvbWVwcm9qZWN0LmV1LmF1dGgwLmNvbS8iLCJhdWQiOiI5QkhMS0cxcENaTkpRZzBjWFRQQ0pJcmdIeUxwNDN6MCIsImlhdCI6MTY4NTI3NTM3NCwiZXhwIjoxNjg1MzExMzc0LCJzdWIiOiJhdXRoMHw2NDRiYTAxYTJkMmYzZDcyYTZlNGE4MWQiLCJzaWQiOiJiQklvZGswWGc5OFphSFBteE5IRlJ4dkM5TVNUZUtKRSIsIm5vbmNlIjoiUlRka1JERnlTMU5vYmw5Mk9EQTJNRkZsZW5CRloxWXlVVzVFT0RsdmJIRnpYMVI1YjBaRlJGWkdjZz09In0.OmrWiPzifeCkk69lH7m0JVPzU-JEbsIxbGy8wW4EZkRChMiaMAgy4di-qOlBtQGe3MtDWhaXu3NlrqK9wzWWQFvcPSzophYVn_8zCQeIAiZN__osUJrXRcRF2jOHnC0MLRptz4AWxLyFKo52yeEdKNewSF7xh6YoWU1N1cdkVIJZN5m9ny8A4jim-y0sC9C9vtrHWqR5U3B1mgVMXQEn02dwWAJmoknBIHeuLOHtTo1VmkY39H1GL_cT2OT05X1o8Co7OhdDyQIgHJFuXhl_xzwMZSoTjqK6mMrDbrq7S7wF3Xk4vDGUeALfxJLuHo3BRXipiFsnL6hYCBppR0t07A";
    static final String SUB = "sub";
    static final String AUTH0ID = "sms|12345678";
    private static final String TEST_EMAIL = "test.abc.xyz@gmail.com";

    @Bean
    public JwtDecoder jwtDecoder() {
        return new JwtDecoder() {
            @Override
            public Jwt decode(String token) {
                return jwt();
            }
        };
    }

    public Jwt jwt() {
        Map<String, Object> claims = new HashMap<>();
        claims.put(SUB, "test");
        claims.put("user_roles", Arrays.asList("customer", "hairdresser"));
        claims.put("email", TEST_EMAIL);
        return new Jwt(
                AUTH0_TOKEN,
                Instant.now(),
                Instant.now().plusSeconds(30),
                Map.of("alg", "none"),
                claims);
    }
}
