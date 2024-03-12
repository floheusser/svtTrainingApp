package ch.zhaw.svtTrainingApp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.zhaw.svtTrainingApp.model.Appointment;
import ch.zhaw.svtTrainingApp.model.AppointmentState;
import ch.zhaw.svtTrainingApp.model.HairdresserTask;
import ch.zhaw.svtTrainingApp.repository.AppointmentRepository;
import ch.zhaw.svtTrainingApp.repository.HairdresserTaskRepository;

@Service
public class AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    HairdresserTaskRepository hairdresserTaskRepository;


    public Optional<Appointment> updateAppointmentState(String appointmentId, AppointmentState state) {
        Optional<Appointment> optAppointment = appointmentRepository.findById(appointmentId);
        if (optAppointment.isPresent()) {
            Appointment appointment = optAppointment.get();
            appointment.setAppointmentState(state);
            appointmentRepository.save(appointment);
            return Optional.of(appointment);
        }

        return Optional.empty();
    }

    public Double calculatePriceForCustomer(List<String> hairdresserTasks) {
        List<HairdresserTask> allTasks = hairdresserTaskRepository.findAll();
        return allTasks.stream()
                .filter(task -> hairdresserTasks.contains(task.getId()))
                .mapToDouble(t -> t.getPrice()).sum();

    }

    public Double calculatePriceForHairdresser(List<String> hairdresserTasks) {
        List<HairdresserTask> allTasks = hairdresserTaskRepository.findAll();
        return allTasks.stream()
                .filter(task -> hairdresserTasks.contains(task.getId()))
                .mapToDouble(t -> t.getPrice()).sum() * 0.90; // 10% margin for hair@home

    }
}
