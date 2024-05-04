package ch.zhaw.svtTrainingApp.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
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

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import ch.zhaw.svtTrainingApp.model.Group;
import ch.zhaw.svtTrainingApp.model.Training;
import ch.zhaw.svtTrainingApp.model.dto.TrainingDTO;
import ch.zhaw.svtTrainingApp.repository.GroupRepository;
import ch.zhaw.svtTrainingApp.repository.TrainingRepository;

@RestController
@RequestMapping("/api")
public class TrainingController {

    @Autowired
    TrainingRepository trainingRepository;

    @Autowired
    GroupRepository groupRepository;

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

    @GetMapping("/trainings/{groupName}")
    public ResponseEntity<List<Training>> getTrainingsByGroup(@PathVariable String groupName) {
        List<Training> trainings = trainingRepository.findAllByGroupNameOrderByDateDesc(groupName);
        if (groupName.equals("all")) {
            trainings = trainingRepository.findAllByOrderByDateDesc();
        }
        return new ResponseEntity<>(trainings, HttpStatus.OK);
    }

    @GetMapping("/trainingsMissing/{groupName}/{sdate}")
    @Secured("ROLE_admin")
    public ResponseEntity<List<LocalDate>> getTrainingsMissingByGroup(@PathVariable String groupName, @PathVariable String sdate) {
         try {
            LocalDate startDate = LocalDate.parse(sdate);
            LocalDate today = LocalDate.now();
            Group group = groupRepository.findFirstByName(groupName);
            
            if (startDate.isAfter(today) || group == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            List<LocalDate> allDates = startDate.datesUntil(today)
                                               .filter(d -> d.getDayOfWeek().equals(group.getWeekday()))
                                               .collect(Collectors.toList());

            List<LocalDate> trainingDates = trainingRepository.findAllByGroupNameOrderByDateDesc(groupName)
                                                .stream()
                                                .map(training -> LocalDate.parse(training.getDate()))
                                                .collect(Collectors.toList());

            List<LocalDate> missingTrainings = allDates.stream()
                                                       .filter(date -> !trainingDates.contains(date))
                                                       .collect(Collectors.toList());

            return new ResponseEntity<>(missingTrainings, HttpStatus.OK);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    
    @GetMapping("/trainings/pdf/{seasonYear}")
    public ResponseEntity<InputStreamResource> downloadTrainingsPdf(@PathVariable String seasonYear) throws DocumentException, IOException {
        List<Training> trainings = trainingRepository.findAllByDateContainsOrderByDateDesc(seasonYear);

        if (seasonYear.equalsIgnoreCase("alle")) {
            trainings = trainingRepository.findAllByOrderByDateDesc();
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        for (Training training : trainings) {
            document.newPage();
            document.add(new Paragraph("Datum: " + training.getDate()));
            document.add(new Paragraph("Gruppe: " + training.getGroupName()));
            document.add(new Paragraph("Trainer: " + training.getTrainerName()));
            document.add(new Paragraph("Hilfstrainer: " + training.getHelpTrainerName()));
            document.add(new Paragraph("Wetter/ Wind: " + training.getWeather()));
            document.add(new Paragraph("Trainingsinhalt: " + training.getTrainingContent()));

            if (training.getTrainingContentPicture() != null) {
                Image img = Image.getInstance(training.getTrainingContentPicture().getData());
                img.scaleToFit(400, 600);
            
                // Center the image on the page
                img.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_MIDDLE);
                document.add(img);
            }
        }

        document.close();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename="+seasonYear+"_Trainingprotokolle.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(new ByteArrayInputStream(out.toByteArray())));
    }
    
}
