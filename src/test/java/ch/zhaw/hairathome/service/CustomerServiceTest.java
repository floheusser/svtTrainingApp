package ch.zhaw.hairathome.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.zhaw.hairathome.model.Customer;
import ch.zhaw.hairathome.model.dto.CustomerChangeDTO;
import ch.zhaw.hairathome.repository.CustomerRepository;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private CustomerChangeDTO customerChangeDTO;
    private Customer customer;

    @BeforeEach
    public void setUp() {  
        MockitoAnnotations.openMocks(this);      
        customerChangeDTO = new CustomerChangeDTO();
        customerChangeDTO.setFirstname("Updated Firstname");
        customerChangeDTO.setLastname("Updated Lastname");
        customerChangeDTO.setCity("Updatetd City");
        customerChangeDTO.setStreet("Updated Street");
        customerChangeDTO.setPhone("Updated Phone");
        customerChangeDTO.setPostCode("Updated Postcode");

        customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setNickname("testNickname");
    }

    @Test
    public void testUpdateCustomer_CustomerExists() {
        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));

        Optional<Customer> updatedCustomer = customerService.updateCustomer(customer.getEmail(), customerChangeDTO);

        assertTrue(updatedCustomer.isPresent());
        assertEquals(customerChangeDTO.getFirstname(), updatedCustomer.get().getFirstname());
        assertEquals(customerChangeDTO.getLastname(), updatedCustomer.get().getLastname());
        assertEquals(customerChangeDTO.getCity(), updatedCustomer.get().getCity());
        assertEquals(customerChangeDTO.getStreet(), updatedCustomer.get().getStreet());
        assertEquals(customerChangeDTO.getPhone(), updatedCustomer.get().getPhone());
        assertEquals(customerChangeDTO.getPostCode(), updatedCustomer.get().getPostCode());
    }

    @Test
    public void testUpdateCustomer_CustomerDoesNotExist() {
        when(customerRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        Optional<Customer> updatedCustomer = customerService.updateCustomer("notfound@example.com", customerChangeDTO);

        assertTrue(updatedCustomer.isEmpty());
    }
}
