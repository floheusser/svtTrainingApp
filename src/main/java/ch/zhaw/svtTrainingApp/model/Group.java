package ch.zhaw.svtTrainingApp.model;

import java.time.DayOfWeek;

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
@Document("trainingGroups")
public class Group {
    
    @Id
    private String id;

    @NonNull
    private String name;

    private DayOfWeek weekday;
}
