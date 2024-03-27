package ch.zhaw.svtTrainingApp.model;

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
@Document("users")
public class AppUser {
    
    @Id
    private String id;

    @NonNull
    private String nickname;
      
    @NonNull
    private String email;

    private List<String> roles;

}
