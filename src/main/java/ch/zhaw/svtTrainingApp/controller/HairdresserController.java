package ch.zhaw.svtTrainingApp.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.svtTrainingApp.model.Hairdresser;
import ch.zhaw.svtTrainingApp.model.dto.HairdresserChangeDTO;
import ch.zhaw.svtTrainingApp.model.dto.UserCreateDTO;
import ch.zhaw.svtTrainingApp.repository.HairdresserRepository;
import ch.zhaw.svtTrainingApp.service.AddressValidatorService;
import ch.zhaw.svtTrainingApp.service.HairdresserService;

@RestController
@RequestMapping("/api")
public class HairdresserController {

    @Autowired
    HairdresserRepository hairdresserRepository;

    @Autowired
    HairdresserService hairdresserService;

    @Autowired
    AddressValidatorService addressValidatorService;

    @PostMapping("/hairdresser")
    @Secured("ROLE_hairdresser")
    public ResponseEntity<Hairdresser> createHairdresser(@RequestBody UserCreateDTO hDTO) {
        Hairdresser hDAO = new Hairdresser(hDTO.getNickname(), hDTO.getEmail());
        Hairdresser hairdresser = hairdresserRepository.save(hDAO);
        return new ResponseEntity<>(hairdresser, HttpStatus.CREATED);
    }

    @PutMapping("/hairdresser/account/update")
    @Secured("ROLE_hairdresser")
    public ResponseEntity<Object> updateHairdresser(@AuthenticationPrincipal Jwt jwt,
            @RequestBody HairdresserChangeDTO cDTO) {
        if (!addressValidatorService.isAddressValid(cDTO.getStreet(), cDTO.getCity(), cDTO.getPostCode())) {
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("timestamp", LocalDateTime.now());
            errorDetails.put("message", "Invalid address! Check Street, Postcode and City");
            return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        }
        Optional<Hairdresser> optHairdresser = hairdresserService.updateHairdresser(jwt.getClaimAsString("email"), cDTO);
        if (optHairdresser.isPresent()) {
            return new ResponseEntity<>(optHairdresser.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/hairdresser/account")
    @Secured("ROLE_hairdresser")
    public ResponseEntity<Hairdresser> getHairdresserByEmail(@AuthenticationPrincipal Jwt jwt) {
        Optional<Hairdresser> optHairdresser = hairdresserRepository.findByEmail(jwt.getClaimAsString("email"));
        if (optHairdresser.isPresent()) {
            return new ResponseEntity<>(optHairdresser.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/data/hairdressers")
    public ResponseEntity<List<Hairdresser>> getAllHairdresser() {
        List<Hairdresser> allHairdressers = hairdresserRepository.findAll();
        return new ResponseEntity<>(allHairdressers, HttpStatus.OK);

    }
    @GetMapping("/hairdressers")
    public ResponseEntity<Page<Hairdresser>> getAllHairdresser(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) List<String> hairdresserTasks,
            @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "2") Integer pageSize) {
        Page<Hairdresser> allHairdressers;
        if (city == null && hairdresserTasks == null) {
            allHairdressers = hairdresserRepository.findAll(PageRequest.of(pageNumber - 1, pageSize));
        } else {
            if (city != null && hairdresserTasks != null ) {
                allHairdressers = hairdresserRepository.findByCityIgnoreCaseAndHairdresserTasksIn(city, hairdresserTasks,
                        PageRequest.of(pageNumber - 1, pageSize));
            } else if (city != null) {
                allHairdressers = hairdresserRepository.findByCityIgnoreCase(city,
                        PageRequest.of(pageNumber - 1, pageSize));
            } else {
                allHairdressers = hairdresserRepository.findByHairdresserTasksIn(hairdresserTasks,
                        PageRequest.of(pageNumber - 1, pageSize));
            }
        }
        return new ResponseEntity<>(allHairdressers, HttpStatus.OK);
    }

    @GetMapping("/hairdresser/{id}")
    public ResponseEntity<Hairdresser> getHairdresserById(@PathVariable String id) {
        Optional<Hairdresser> optHairdressers = hairdresserRepository.findById(id);
        if (optHairdressers.isPresent()) {
            return new ResponseEntity<>(optHairdressers.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
