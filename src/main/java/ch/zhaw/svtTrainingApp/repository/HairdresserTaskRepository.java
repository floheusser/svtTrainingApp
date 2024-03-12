package ch.zhaw.svtTrainingApp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import ch.zhaw.svtTrainingApp.model.HairdresserTask;

public interface HairdresserTaskRepository extends MongoRepository<HairdresserTask, String> {
    
}
