package ch.zhaw.svtTrainingApp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import ch.zhaw.svtTrainingApp.model.Appointment;
import ch.zhaw.svtTrainingApp.model.AppointmentState;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {
    
    List<Appointment> findByHairdresserIdAndAppointmentState(String hairdresserId, AppointmentState state);
    List<Appointment> findByCustomerIdAndAppointmentState(String hairdresserId, AppointmentState state);
}
