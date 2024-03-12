package ch.zhaw.svtTrainingApp.model;

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
@Document("customer")
public class Customer {
    
    @Id
    private String id;
  
    private String firstname;

    private String lastname;

    private String phone;

    @NonNull
    private String nickname;
      
    @NonNull
    private String email;

    private String street;
    private String city;
    private String postCode;
}
