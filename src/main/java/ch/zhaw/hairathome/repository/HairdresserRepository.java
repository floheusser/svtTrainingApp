package ch.zhaw.hairathome.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import ch.zhaw.hairathome.model.Hairdresser;

public interface HairdresserRepository extends MongoRepository<Hairdresser, String> {
    
    Hairdresser findFirstByEmail(String email);
    Optional<Hairdresser> findByEmail(String email);
    Page<Hairdresser> findByHairdresserTasksIn(List<String> hairdressertasks, Pageable pageable);
    Page<Hairdresser> findByCityIgnoreCase(String city, Pageable pageable);
    Page<Hairdresser> findByCityIgnoreCaseAndHairdresserTasksIn(String city, List<String> hairdressertasks, Pageable pageable);
}
