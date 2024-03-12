package ch.zhaw.svtTrainingApp.model.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AppointmentCreateDTO {
    private String customerEmail;
    private String hairdresserId;
    private String date;
    private String time;
    private List<String> hairdresserTasks = new ArrayList<>();
}
