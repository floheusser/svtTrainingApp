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
@Document("appointment")
public class Appointment {
 
    @Id
    private String id;

    @NonNull
    private String customerId;

    @NonNull
    private String hairdresserId;
    
    @NonNull
    private String date;

    @NonNull
    private String time;

    @NonNull
    private List<String> hairdresserTasks = new ArrayList<>();

    private AppointmentState appointmentState = AppointmentState.REQUESTED;

    private Double hairdresserPrice;
    private Double customerPrice;

    public void addTask(String taskId) {
        hairdresserTasks.add(taskId);
    }

}
