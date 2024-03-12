package ch.zhaw.svtTrainingApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.svtTrainingApp.model.HairdresserTask;
import ch.zhaw.svtTrainingApp.model.dto.HairdresserTaskCreateDTO;
import ch.zhaw.svtTrainingApp.repository.HairdresserTaskRepository;

@RestController
@RequestMapping("/api")
public class HairdresserTaskController {

    @Autowired
    HairdresserTaskRepository hairdresserTaskRepository;

    @PostMapping("/hairdresserTask")
    public ResponseEntity<HairdresserTask> createHairdresserTask(@RequestBody HairdresserTaskCreateDTO htDTO) {
        HairdresserTask htDAO = new HairdresserTask(htDTO.getName(), htDTO.getPrice());
        HairdresserTask hairdresserTask = hairdresserTaskRepository.save(htDAO);
        return new ResponseEntity<>(hairdresserTask, HttpStatus.CREATED);
    }

    @GetMapping("/hairdresserTasks")
    public ResponseEntity<List<HairdresserTask>> getAllHairdresserTasks() {
        List<HairdresserTask> allHairdresserTasks = hairdresserTaskRepository.findAll();
        return new ResponseEntity<>(allHairdresserTasks, HttpStatus.OK);
    }
}
