package ch.zhaw.hairathome.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.zhaw.hairathome.model.Customer;
import ch.zhaw.hairathome.model.dto.CustomerChangeDTO;
import ch.zhaw.hairathome.repository.CustomerRepository;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public Optional<Customer> updateCustomer(String email, CustomerChangeDTO cDTO) {
        Optional<Customer> optCustomer = customerRepository.findByEmail(email);
        if (optCustomer.isPresent()) {
            Customer customer = optCustomer.get();
            customer.setFirstname(cDTO.getFirstname());
            customer.setLastname(cDTO.getLastname());
            customer.setPhone(cDTO.getPhone());
            customer.setStreet(cDTO.getStreet());
            customer.setCity(cDTO.getCity());
            customer.setPostCode(cDTO.getPostCode());
            customerRepository.save(customer);
            return Optional.of(customer);
        }
        return Optional.empty();
    }
}
