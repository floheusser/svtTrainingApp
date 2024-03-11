package ch.zhaw.hairathome.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.zhaw.hairathome.model.Hairdresser;
import ch.zhaw.hairathome.model.dto.HairdresserChangeDTO;
import ch.zhaw.hairathome.repository.HairdresserRepository;

public class HairdresserServiceTest {

    @Mock
    private HairdresserRepository hairdresserRepository;

    @InjectMocks
    private HairdresserService hairdresserService;

    private HairdresserChangeDTO hairdresserChangeDTO;
    private Hairdresser hairdresser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); 

        hairdresserChangeDTO = new HairdresserChangeDTO();
        hairdresserChangeDTO.setFirstname("Updated Firstname");
        hairdresserChangeDTO.setLastname("Updated Lastname");
        hairdresserChangeDTO.setCity("Updatetd City");
        hairdresserChangeDTO.setStreet("Updated Street");
        hairdresserChangeDTO.setPhone("Updated Phone");
        hairdresserChangeDTO.setPostCode("Updated Postcode");
        hairdresserChangeDTO.setAboutMeText("Updated About Me Text");
        hairdresserChangeDTO.setHairdresserTasks(Arrays.asList("update1", "update2"));

        hairdresser = new Hairdresser();
        hairdresser.setEmail("test@example.com");
    }

    @Test
    public void testUpdateHairdresser_HairdresserExists() {
        when(hairdresserRepository.findByEmail(hairdresser.getEmail())).thenReturn(Optional.of(hairdresser));

        Optional<Hairdresser> updatedHairdresser = hairdresserService.updateHairdresser(hairdresser.getEmail(), hairdresserChangeDTO);

        assertTrue(updatedHairdresser.isPresent());
        assertEquals(hairdresserChangeDTO.getFirstname(), updatedHairdresser.get().getFirstname());
        assertEquals(hairdresserChangeDTO.getLastname(), updatedHairdresser.get().getLastname());
        assertEquals(hairdresserChangeDTO.getCity(), updatedHairdresser.get().getCity());
        assertEquals(hairdresserChangeDTO.getStreet(), updatedHairdresser.get().getStreet());
        assertEquals(hairdresserChangeDTO.getPhone(), updatedHairdresser.get().getPhone());
        assertEquals(hairdresserChangeDTO.getPostCode(), updatedHairdresser.get().getPostCode());
        assertEquals(hairdresserChangeDTO.getAboutMeText(), updatedHairdresser.get().getAboutMeText());
        assertEquals(hairdresserChangeDTO.getHairdresserTasks(), updatedHairdresser.get().getHairdresserTasks());
    }

    @Test
    public void testUpdateHairdresser_HairdresserDoesNotExist() {
        when(hairdresserRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        Optional<Hairdresser> updatedHairdresser = hairdresserService.updateHairdresser("notfound@example.com", hairdresserChangeDTO);

        assertTrue(updatedHairdresser.isEmpty());
    }
}
