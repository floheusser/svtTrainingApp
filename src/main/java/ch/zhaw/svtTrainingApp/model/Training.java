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
@Document("training")
public class Training {
    
    @Id
    private String id;

    @NonNull
    private String userEmail;

    @NonNull
    private String trainerName;

    @NonNull
    private String groupName;

    @NonNull
    private String helpTrainerName;

    @NonNull
    private String date;

    @NonNull
    private String weather;

    private String trainingContentPicture;

    private String trainingContent;

   // Datum, Trainingskategorie, Name Trainer, Name Hilfstrai-ner, Teilnehmer, Wind/Wetter, Trainingsablauf

}
