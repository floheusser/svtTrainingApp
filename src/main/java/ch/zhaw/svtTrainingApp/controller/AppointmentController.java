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

import ch.zhaw.svtTrainingApp.model.Appointment;
import ch.zhaw.svtTrainingApp.model.AppointmentState;
import ch.zhaw.svtTrainingApp.model.Customer;
import ch.zhaw.svtTrainingApp.model.Hairdresser;
import ch.zhaw.svtTrainingApp.model.dto.AppointmentCreateDTO;
import ch.zhaw.svtTrainingApp.model.dto.AppointmentStateChangeDTO;
import ch.zhaw.svtTrainingApp.repository.AppointmentRepository;
import ch.zhaw.svtTrainingApp.repository.CustomerRepository;
import ch.zhaw.svtTrainingApp.repository.HairdresserRepository;
import ch.zhaw.svtTrainingApp.service.AppointmentService;

@RestController
@RequestMapping("/api")
public class AppointmentController {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    HairdresserRepository hairdresserRepository;

    @Autowired
    AppointmentService appoinmentService;

    @PostMapping("/appointment")
    @Secured("ROLE_customer")
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentCreateDTO aDTO) {
        Appointment aDAO = new Appointment(customerRepository.findFirstByEmail(aDTO.getCustomerEmail()).getId(),
                aDTO.getHairdresserId(), aDTO.getDate(), aDTO.getTime());
        aDTO.getHairdresserTasks().forEach(t -> aDAO.addTask(t));
        aDAO.setCustomerPrice(appoinmentService.calculatePriceForCustomer(aDAO.getHairdresserTasks()));
        aDAO.setHairdresserPrice(appoinmentService.calculatePriceForHairdresser(aDAO.getHairdresserTasks()));
        Appointment appointment = appointmentRepository.save(aDAO);
        return new ResponseEntity<>(appointment, HttpStatus.CREATED);
    }

    @GetMapping("/appointment/myRequestedAppointments")
    public ResponseEntity<List<Appointment>> getRequestedAppointments(@AuthenticationPrincipal Jwt jwt) {
        Optional<Hairdresser> optHairdresser = hairdresserRepository.findByEmail(jwt.getClaimAsString("email"));
        Optional<Customer> optCustomer = customerRepository.findByEmail(jwt.getClaimAsString("email"));
        if (optHairdresser.isPresent()) {
            List<Appointment> allAppointments = appointmentRepository
                    .findByHairdresserIdAndAppointmentState(optHairdresser.get().getId(), AppointmentState.REQUESTED);
            return new ResponseEntity<>(allAppointments, HttpStatus.OK);
        }
        if (optCustomer.isPresent()) {
            List<Appointment> allAppointments = appointmentRepository
                    .findByCustomerIdAndAppointmentState(optCustomer.get().getId(), AppointmentState.REQUESTED);
            return new ResponseEntity<>(allAppointments, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/appointment/myApprovedAppointments")
    public ResponseEntity<List<Appointment>> getApprovedAppointments(@AuthenticationPrincipal Jwt jwt) {
        Optional<Hairdresser> optHairdresser = hairdresserRepository.findByEmail(jwt.getClaimAsString("email"));
        Optional<Customer> optCustomer = customerRepository.findByEmail(jwt.getClaimAsString("email"));
        if (optHairdresser.isPresent()) {
            List<Appointment> allAppointments = appointmentRepository
                    .findByHairdresserIdAndAppointmentState(optHairdresser.get().getId(), AppointmentState.APPROVED);
            return new ResponseEntity<>(allAppointments, HttpStatus.OK);
        }
        if (optCustomer.isPresent()) {
            List<Appointment> allAppointments = appointmentRepository
                    .findByCustomerIdAndAppointmentState(optCustomer.get().getId(), AppointmentState.APPROVED);
            return new ResponseEntity<>(allAppointments, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/appointment/updateAppointmentState")
    public ResponseEntity<Appointment> updateAppointmentState(@AuthenticationPrincipal Jwt jwt,
            @RequestBody AppointmentStateChangeDTO cDTO) {
        Optional<Appointment> optAppointment = appoinmentService.updateAppointmentState(cDTO.getAppointmentId(),
                cDTO.getState());
        if (optAppointment.isPresent()) {
            return new ResponseEntity<>(optAppointment.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
