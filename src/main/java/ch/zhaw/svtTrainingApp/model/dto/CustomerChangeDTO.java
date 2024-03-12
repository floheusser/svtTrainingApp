package ch.zhaw.svtTrainingApp.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class CustomerChangeDTO {
    
    private String firstname;
    private String lastname;
    private String phone;
    private String street;
    private String city;
    private String postCode;
}
