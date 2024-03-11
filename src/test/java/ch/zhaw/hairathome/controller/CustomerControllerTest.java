package ch.zhaw.hairathome.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import ch.zhaw.hairathome.model.Customer;
import ch.zhaw.hairathome.model.dto.CustomerChangeDTO;
import ch.zhaw.hairathome.model.dto.UserCreateDTO;
import ch.zhaw.hairathome.repository.CustomerRepository;
import ch.zhaw.hairathome.security.TestSecurityConfig;
import ch.zhaw.hairathome.service.AddressValidatorService;
import ch.zhaw.hairathome.service.CustomerService;

@SpringBootTest
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    CustomerRepository customerRepository;

    @MockBean
    CustomerService customerService;

    @Autowired
    AddressValidatorService addressValidatorService;

    private static final String TEST_EMAIL = "test.abc.xyz@gmail.com";
    private static final String TEST_NICKNAME = "TEST-abc...xyz";
    private static ObjectMapper mapper = new ObjectMapper();
    private static ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

    @Test
    @Order(1)
    @WithMockUser(roles = "customer")
    public void testCreateCustomer() throws Exception {
        UserCreateDTO customerDto = new UserCreateDTO();
        customerDto.setEmail(TEST_EMAIL);
        customerDto.setNickname(TEST_NICKNAME);

        String jsonBody = ow.writeValueAsString(customerDto);

        mvc.perform(post("/api/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    @Order(2)
    @WithMockUser(roles = "customer")
    public void testUpdateCustomer() throws Exception {
        CustomerChangeDTO customerChangeDTO = new CustomerChangeDTO();

        customerChangeDTO.setStreet("Alte Landstrasse 198");
        customerChangeDTO.setCity("Thalwil");
        customerChangeDTO.setPostCode("8800");
        customerChangeDTO.setPhone("000000000");

        Customer custo = new Customer(TEST_NICKNAME, TEST_EMAIL);

        when(customerService.updateCustomer(any(), any())).thenReturn(Optional.of(custo));
        String jsonBody = ow.writeValueAsString(customerChangeDTO);

        mvc.perform(put("/api/customer/account/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Order(3)
    @WithMockUser(roles = "customer")
    public void testGetCustomerByEmail() throws Exception {
        mvc.perform(get("/api/customer/account")
                .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Order(4)
    @WithMockUser(roles = "customer")
    public void testGetAllCustomers() throws Exception {
        mvc.perform(get("/api/customers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Order(5)
    @WithMockUser(roles = "customer")
    public void testDeleteCustomer() throws Exception {
        Optional<Customer> result = customerRepository.findByEmail(TEST_EMAIL);
        if (result.isPresent()) {
            customerRepository.deleteById(result.get().getId());
        }

        result = customerRepository.findByEmail(TEST_EMAIL);
        assertTrue(result.isEmpty());
    }    
}
