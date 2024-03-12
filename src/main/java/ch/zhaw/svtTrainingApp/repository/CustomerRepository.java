package ch.zhaw.svtTrainingApp.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import ch.zhaw.svtTrainingApp.model.Customer;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    
    Customer findFirstByEmail(String email);
    Optional<Customer> findByEmail(String email);
}
