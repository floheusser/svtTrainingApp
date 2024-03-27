package ch.zhaw.svtTrainingApp.model;

import java.util.Date;

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
@Document("trainingProtocols")
public class TrainingProtocol {
    
    @Id
    private String id;

    @NonNull
    private String trainerName;

    @NonNull
    private String groupName;

    private String helpTrainerName;

    private Date date;

    private String weather;

   // Datum, Trainingskategorie, Name Trainer, Name Hilfstrai-ner, Teilnehmer, Wind/Wetter, Trainingsablauf

}
