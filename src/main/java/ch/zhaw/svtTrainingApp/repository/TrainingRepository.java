package ch.zhaw.svtTrainingApp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import ch.zhaw.svtTrainingApp.model.Training;


public interface TrainingRepository extends MongoRepository<Training, String> {
    
    List<Training> findAllByUserEmailOrderByDateDesc(String email);

    List<Training> findAllByOrderByDateDesc();

    List<Training> findAllByGroupNameOrderByDateDesc(String groupName);

}
