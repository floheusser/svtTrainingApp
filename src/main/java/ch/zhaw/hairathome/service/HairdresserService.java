package ch.zhaw.hairathome.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.zhaw.hairathome.model.Hairdresser;
import ch.zhaw.hairathome.model.dto.HairdresserChangeDTO;
import ch.zhaw.hairathome.repository.HairdresserRepository;

@Service
public class HairdresserService {
    
    @Autowired
    HairdresserRepository hairdresserRepository;

    public Optional<Hairdresser> updateHairdresser(String email, HairdresserChangeDTO cDTO) {
        Optional<Hairdresser> optHairdresser = hairdresserRepository.findByEmail(email);
        if(optHairdresser.isPresent()) {
            Hairdresser hairdresser = optHairdresser.get();
            hairdresser.setFirstname(cDTO.getFirstname());
            hairdresser.setLastname(cDTO.getLastname());
            hairdresser.setPhone(cDTO.getPhone());
            hairdresser.setStreet(cDTO.getStreet());
            hairdresser.setCity(cDTO.getCity());
            hairdresser.setPostCode(cDTO.getPostCode());
            hairdresser.setAboutMeText(cDTO.getAboutMeText());
            hairdresser.setHairdresserTasks(cDTO.getHairdresserTasks());
            hairdresserRepository.save(hairdresser);
            return Optional.of(hairdresser);
        }
        return Optional.empty();
    }
}
