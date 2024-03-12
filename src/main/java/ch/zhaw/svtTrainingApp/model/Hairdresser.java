package ch.zhaw.svtTrainingApp.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Document("hairdresser")
public class Hairdresser {
    
    @Id
    private String id;
  
    private String firstname;

    private String lastname;

    @NonNull
    private String nickname;

    @NonNull
    private String email;

    private String phone;

    private String street;
    private String city;
    private String postCode;
    private String aboutMeText;
    private List<String> hairdresserTasks = new ArrayList<>();
    
}
