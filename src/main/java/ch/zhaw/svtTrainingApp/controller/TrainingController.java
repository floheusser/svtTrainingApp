package ch.zhaw.svtTrainingApp.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ch.zhaw.svtTrainingApp.model.Training;
import ch.zhaw.svtTrainingApp.model.dto.TrainingDTO;
import ch.zhaw.svtTrainingApp.repository.TrainingRepository;

@RestController
@RequestMapping("/api")
public class TrainingController {

    @Autowired
    TrainingRepository trainingRepository;

    @PostMapping(value = "/user/training", consumes = "multipart/form-data")
    @Secured("ROLE_trainer")
    public ResponseEntity<Training> createTraining(@AuthenticationPrincipal Jwt jwt,
                                            @RequestPart("trainingData") TrainingDTO tDTO,
                                            @RequestPart("file") MultipartFile file) throws IOException {
        Training tDAO = new Training(jwt.getClaimAsString("email"), tDTO.getTrainerName(), tDTO.getGroupName(), tDTO.getHelpTrainerName(), tDTO.getDate(), tDTO.getWeather());
        tDAO.setTrainingContent(tDTO.getTrainingContent());
        if (file != null) {
           tDAO.setTrainingContentPicture(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        }
        Training training = trainingRepository.save(tDAO);
        return new ResponseEntity<>(training, HttpStatus.CREATED);
    }

    @PutMapping(value = "/user/training", consumes = "multipart/form-data")
    @Secured("ROLE_trainer")
    public ResponseEntity<Object> updateTraining(@AuthenticationPrincipal Jwt jwt,
                                                 @RequestPart("trainingData") TrainingDTO tDTO,
                                                 @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        Optional<Training> optTraining = trainingRepository.findById(tDTO.getId());
        if (optTraining.isPresent()) {
            Training tDAO = optTraining.get();
            tDAO.setDate(tDTO.getDate());
            tDAO.setGroupName(tDTO.getGroupName());
            tDAO.setTrainerName(tDTO.getTrainerName());
            tDAO.setWeather(tDTO.getWeather());
            tDAO.setHelpTrainerName(tDTO.getHelpTrainerName());
            tDAO.setTrainingContent(tDTO.getTrainingContent());
            
            if (file != null && !file.isEmpty()) {
                tDAO.setTrainingContentPicture(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
            }
    
            Training training = trainingRepository.save(tDAO);
            return new ResponseEntity<>(training, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    
    @GetMapping("/user/training/{id}/image")
    public ResponseEntity<byte[]> getTrainingImage(@PathVariable String id) {
        return trainingRepository.findById(id)
            .map(training -> {
                if (training.getTrainingContentPicture() != null) {
                    byte[] bytes = training.getTrainingContentPicture().getData();
                    return ResponseEntity.ok()
                            .contentType(MediaType.IMAGE_JPEG) // Stellen Sie sicher, dass der Medientyp dem Bildtyp entspricht
                            .body(bytes);
                } else {
                    return ResponseEntity.ok(new byte[0]); // Leeres Byte-Array zurückgeben, wenn kein Bild vorhanden ist
                }
            })
            .orElse(ResponseEntity.notFound().build());
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
