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
import ch.zhaw.hairathome.model.Hairdresser;
import ch.zhaw.hairathome.model.dto.HairdresserChangeDTO;
import ch.zhaw.hairathome.model.dto.UserCreateDTO;
import ch.zhaw.hairathome.repository.CustomerRepository;
import ch.zhaw.hairathome.repository.HairdresserRepository;
import ch.zhaw.hairathome.security.TestSecurityConfig;
import ch.zhaw.hairathome.service.AddressValidatorService;
import ch.zhaw.hairathome.service.HairdresserService;

@SpringBootTest
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class HairdresserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    HairdresserRepository hairdresserRepository;

    @Autowired
    CustomerRepository customerRepository;

    @MockBean
    HairdresserService hairdresserService;

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
    @WithMockUser(roles = "hairdresser")
    public void testCreateHairdresser() throws Exception {
        UserCreateDTO hairdresserDto = new UserCreateDTO();
        hairdresserDto.setEmail(TEST_EMAIL);
        hairdresserDto.setNickname(TEST_NICKNAME);

        String jsonBody = ow.writeValueAsString(hairdresserDto);

        mvc.perform(post("/api/hairdresser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    @Order(3)
    @WithMockUser(roles = "hairdresser")
    public void testUpdateHairdresser() throws Exception {
        HairdresserChangeDTO hairdresserChangeDTO = new HairdresserChangeDTO();
        hairdresserChangeDTO.setStreet("Alte Landstrasse 198");
        hairdresserChangeDTO.setCity("Thalwil");
        hairdresserChangeDTO.setPostCode("8800");
        hairdresserChangeDTO.setPhone("000000000");
        hairdresserChangeDTO.setAboutMeText("BLa Bla");

        Hairdresser hairdresser = new Hairdresser(TEST_NICKNAME, TEST_EMAIL);

        when(hairdresserService.updateHairdresser(any(), any())).thenReturn(Optional.of(hairdresser));
        String jsonBody = ow.writeValueAsString(hairdresserChangeDTO);

        mvc.perform(put("/api/hairdresser/account/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Order(4)
    @WithMockUser(roles = "hairdresser")
    public void testGetHairdresserByEmail() throws Exception {
        mvc.perform(get("/api/hairdresser/account")
                .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Order(5)
    @WithMockUser(roles = "hairdresser")
    public void testGetAllHairdressers() throws Exception {
        mvc.perform(get("/api/data/hairdressers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Order(6)
    @WithMockUser(roles = "hairdresser")
    public void testGetHairdresserById() throws Exception {
        Optional<Hairdresser> result = hairdresserRepository.findByEmail(TEST_EMAIL);
        if (result.isPresent()) {
            mvc.perform(get("/api/hairdresser/" + result.get().getId())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();
        }
    }  
    
    @Test
    @Order(7)
    @WithMockUser(roles = "hairdresser")
    public void testDeleteHairdresser() throws Exception {
        Optional<Hairdresser> result = hairdresserRepository.findByEmail(TEST_EMAIL);
        if (result.isPresent()) {
            hairdresserRepository.deleteById(result.get().getId());
        }

        result = hairdresserRepository.findByEmail(TEST_EMAIL);
        assertTrue(result.isEmpty());
    } 

    @Test
    @Order(8)
    @WithMockUser(roles = "hairdresser")
    public void testDeleteCustomer() throws Exception {
        Optional<Customer> result = customerRepository.findByEmail(TEST_EMAIL);
        if (result.isPresent()) {
            customerRepository.deleteById(result.get().getId());
        }

        result = customerRepository.findByEmail(TEST_EMAIL);
        assertTrue(result.isEmpty());
    } 
}
