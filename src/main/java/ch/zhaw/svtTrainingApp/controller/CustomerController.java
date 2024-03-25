package ch.zhaw.svtTrainingApp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.svtTrainingApp.model.Customer;
import ch.zhaw.svtTrainingApp.model.dto.CustomerChangeDTO;
import ch.zhaw.svtTrainingApp.model.dto.UserCreateDTO;
import ch.zhaw.svtTrainingApp.repository.CustomerRepository;
import ch.zhaw.svtTrainingApp.service.CustomerService;

@RestController
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerService customerService;

    @PostMapping("/customer")
    @Secured("ROLE_customer")
    public ResponseEntity<Customer> createCustomer(@RequestBody UserCreateDTO cDTO) {
        Customer cDAO = new Customer(cDTO.getNickname(), cDTO.getEmail());
        Customer customer = customerRepository.save(cDAO);
        return new ResponseEntity<>(customer, HttpStatus.CREATED);
    }

    @PutMapping("/customer/account/update")
    @Secured("ROLE_customer")
    public ResponseEntity<Object> updateCustomer(@AuthenticationPrincipal Jwt jwt,
            @RequestBody CustomerChangeDTO cDTO) {
        
        Optional<Customer> optCustomer = customerService.updateCustomer(jwt.getClaimAsString("email"), cDTO);
        if (optCustomer.isPresent()) {
            return new ResponseEntity<>(optCustomer.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/customer/account")
    @Secured("ROLE_customer")
    public ResponseEntity<Customer> getCustomerByEmail(@AuthenticationPrincipal Jwt jwt) {
        Optional<Customer> optCustomer = customerRepository.findByEmail(jwt.getClaimAsString("email"));
        if (optCustomer.isPresent()) {
            return new ResponseEntity<>(optCustomer.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> allCustomers = customerRepository.findAll();
        return new ResponseEntity<>(allCustomers, HttpStatus.OK);
    }
}
