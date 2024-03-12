package ch.zhaw.svtTrainingApp.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserCreateDTO {
    
    private String nickname;
    private String email;
}
