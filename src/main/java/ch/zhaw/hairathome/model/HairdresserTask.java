package ch.zhaw.hairathome.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Document("hairdresserTask")
public class HairdresserTask {
    
    @Id
    private String id;

    @NonNull
    private String name;

    @NonNull
    private Double price;
    
}
