package ch.zhaw.hairathome.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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

import ch.zhaw.hairathome.model.Appointment;
import ch.zhaw.hairathome.model.AppointmentState;
import ch.zhaw.hairathome.model.Customer;
import ch.zhaw.hairathome.model.dto.AppointmentCreateDTO;
import ch.zhaw.hairathome.model.dto.AppointmentStateChangeDTO;
import ch.zhaw.hairathome.model.dto.UserCreateDTO;
import ch.zhaw.hairathome.repository.AppointmentRepository;
import ch.zhaw.hairathome.repository.CustomerRepository;
import ch.zhaw.hairathome.security.TestSecurityConfig;
import ch.zhaw.hairathome.service.AppointmentService;

@SpringBootTest
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    AppointmentRepository appointmentRepository;

    @MockBean
    AppointmentService appointmentService;


    @Autowired
    CustomerRepository customerRepository;

    private static final String TEST_EMAIL = "customer.abc.xyz@gmail.com";
    private static ObjectMapper mapper = new ObjectMapper();
    private static ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

    @Test
    @Order(1)
    @WithMockUser(roles = "customer")
    public void testCreateCustomer() throws Exception {
        UserCreateDTO customerDto = new UserCreateDTO();
        customerDto.setEmail(TEST_EMAIL);
        customerDto.setNickname("TEST_NICKNAME");

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
    public void testCreateAppointment() throws Exception {
        AppointmentCreateDTO appointmentDto = new AppointmentCreateDTO();
        appointmentDto.setCustomerEmail(TEST_EMAIL);
        appointmentDto.setHairdresserId("TEST_HAIRDRESSER_ID");
        appointmentDto.setDate("TEST_DATE");
        appointmentDto.setTime("TEST_TIME");

        when(appointmentService.calculatePriceForCustomer(any())).thenReturn(12.0);
        when(appointmentService.calculatePriceForHairdresser(any())).thenReturn(10.0);


        String jsonBody = ow.writeValueAsString(appointmentDto);

        mvc.perform(post("/api/appointment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    @Order(5)
    @WithMockUser(roles = "customer")
    public void testUpdateAppointmentState() throws Exception {
        AppointmentStateChangeDTO stateChangeDto = new AppointmentStateChangeDTO();
        stateChangeDto.setAppointmentId("TEST");
        stateChangeDto.setState(AppointmentState.TEST);
        Appointment appointment = new Appointment("customerId", "hairdresserid", "date", "timee");
        appointment.setAppointmentState(AppointmentState.TEST);
        when(appointmentService.updateAppointmentState(stateChangeDto.getAppointmentId(), stateChangeDto.getState())).thenReturn(Optional.of(appointment));
        String jsonBody = ow.writeValueAsString(stateChangeDto);

        mvc.perform(put("/api/appointment/updateAppointmentState")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Order(6)
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
