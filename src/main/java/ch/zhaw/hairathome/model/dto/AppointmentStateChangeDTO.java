package ch.zhaw.hairathome.model.dto;

import ch.zhaw.hairathome.model.AppointmentState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AppointmentStateChangeDTO {
    private String appointmentId;
    private AppointmentState state;
}