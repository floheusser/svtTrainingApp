package ch.zhaw.hairathome.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class HairdresserChangeDTO {
    
    private String firstname;
    private String lastname;
    private String phone;
    private String street;
    private String city;
    private String postCode;
    private String aboutMeText;
    private List<String> hairdresserTasks; 
}
