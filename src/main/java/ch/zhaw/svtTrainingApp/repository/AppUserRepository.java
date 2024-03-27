package ch.zhaw.svtTrainingApp.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import ch.zhaw.svtTrainingApp.model.AppUser;


public interface AppUserRepository extends MongoRepository<AppUser, String> {
    
    AppUser findFirstByEmail(String email);
    Optional<AppUser> findByEmail(String email);
}
