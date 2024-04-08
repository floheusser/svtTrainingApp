package ch.zhaw.svtTrainingApp.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TrainingDTO {
    
    private String id;
    private String trainerName;
    private String groupName;
    private String helpTrainerName;
    private String date;
    private String weather;
    private String trainingContentPicture;
    private String trainingContent;
}
