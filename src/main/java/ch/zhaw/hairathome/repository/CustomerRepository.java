package ch.zhaw.hairathome.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import ch.zhaw.hairathome.model.Customer;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    
    Customer findFirstByEmail(String email);
    Optional<Customer> findByEmail(String email);
}
