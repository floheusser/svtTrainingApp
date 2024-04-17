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

import ch.zhaw.svtTrainingApp.model.Training;
import ch.zhaw.svtTrainingApp.model.dto.TrainingDTO;
import ch.zhaw.svtTrainingApp.repository.TrainingRepository;

@RestController
@RequestMapping("/api")
public class TrainingController {

    @Autowired
    TrainingRepository trainingRepository;

    @PostMapping("/user/training")
    @Secured("ROLE_trainer")
    public ResponseEntity<Training> createTraining(@AuthenticationPrincipal Jwt jwt, @RequestBody TrainingDTO tDTO) {
        Training tDAO = new Training(jwt.getClaimAsString("email"), tDTO.getTrainerName(), tDTO.getGroupName(), tDTO.getHelpTrainerName(), tDTO.getDate(), tDTO.getWeather());
        tDAO.setTrainingContent(tDTO.getTrainingContent());
        tDAO.setTrainingContentPicture(tDTO.getTrainingContentPicture());
        Training training = trainingRepository.save(tDAO);
        return new ResponseEntity<>(training, HttpStatus.CREATED);
    }

    @PutMapping("/user/training")
    @Secured("ROLE_trainer")
    public ResponseEntity<Object> updateTraining(@AuthenticationPrincipal Jwt jwt, @RequestBody TrainingDTO tDTO) {
        Optional<Training> optTraining = trainingRepository.findById(tDTO.getId());
        if (optTraining.isPresent()) {
            Training tDAO = optTraining.get();
            tDAO.setDate(tDTO.getDate());
            tDAO.setGroupName(tDTO.getGroupName());
            tDAO.setTrainerName(tDTO.getTrainerName());
            tDAO.setWeather(tDTO.getWeather());
            tDAO.setHelpTrainerName(tDTO.getHelpTrainerName());
            tDAO.setTrainingContent(tDTO.getTrainingContent());
            tDAO.setTrainingContentPicture(tDTO.getTrainingContentPicture());
            Training training = trainingRepository.save(tDAO);
            return new ResponseEntity<>(training, HttpStatus.OK); 
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
    }

    @GetMapping("/user/myTrainings")
    public ResponseEntity<List<Training>> getAllTrainingsFromUser(@AuthenticationPrincipal Jwt jwt) {
        List<Training> allMyTrainings = trainingRepository.findAllByUserEmailOrderByDateDesc(jwt.getClaimAsString("email"));
        return new ResponseEntity<>(allMyTrainings, HttpStatus.OK);
    }

    @GetMapping("/trainings")
    public ResponseEntity<List<Training>> getAllTrainings() {
        List<Training> allTrainings = trainingRepository.findAllByOrderByDateDesc();
        return new ResponseEntity<>(allTrainings, HttpStatus.OK);
    }
}
