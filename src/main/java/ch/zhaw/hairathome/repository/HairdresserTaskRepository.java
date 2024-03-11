package ch.zhaw.hairathome.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import ch.zhaw.hairathome.model.HairdresserTask;

public interface HairdresserTaskRepository extends MongoRepository<HairdresserTask, String> {
    
}
