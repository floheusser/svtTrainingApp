package ch.zhaw.svtTrainingApp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import ch.zhaw.svtTrainingApp.model.Group;


public interface GroupRepository extends MongoRepository<Group, String> {
    
}
