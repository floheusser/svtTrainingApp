package ch.zhaw.hairathome.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.zhaw.hairathome.model.Appointment;
import ch.zhaw.hairathome.model.AppointmentState;
import ch.zhaw.hairathome.model.HairdresserTask;
import ch.zhaw.hairathome.repository.AppointmentRepository;
import ch.zhaw.hairathome.repository.HairdresserTaskRepository;

public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private HairdresserTaskRepository hairdresserTaskRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private Appointment appointment;
    private HairdresserTask task1;
    private HairdresserTask task2;
    private List<HairdresserTask> allTasks;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        appointment = new Appointment();
        appointment.setId(UUID.randomUUID().toString());
        appointment.setAppointmentState(AppointmentState.REQUESTED);

        task1 = new HairdresserTask("task1", 100.0);
        task2 = new HairdresserTask("task2", 200.0);

        allTasks = Arrays.asList(task1, task2);
    }

    @Test
    public void testUpdateAppointmentState_AppointmentExists() {
        when(appointmentRepository.findById(appointment.getId())).thenReturn(Optional.of(appointment));

        Optional<Appointment> updatedAppointment = appointmentService.updateAppointmentState(appointment.getId(), AppointmentState.TEST);

        assertTrue(updatedAppointment.isPresent());
        assertEquals(AppointmentState.TEST, updatedAppointment.get().getAppointmentState());
    }

    @Test
    public void testUpdateAppointmentState_AppointmentDoesNotExist() {
        when(appointmentRepository.findById("invalidId")).thenReturn(Optional.empty());

        Optional<Appointment> updatedAppointment = appointmentService.updateAppointmentState("invalidId", AppointmentState.TEST);

        assertTrue(updatedAppointment.isEmpty());
    }

    @Test
    public void testCalculatePriceForCustomer() {
        when(hairdresserTaskRepository.findAll()).thenReturn(allTasks);

        Double price = appointmentService.calculatePriceForCustomer(Arrays.asList(task1.getId(), task2.getId()));

        assertEquals(300.0, price);
    }

    @Test
    public void testCalculatePriceForHairdresser() {
        when(hairdresserTaskRepository.findAll()).thenReturn(allTasks);

        Double price = appointmentService.calculatePriceForHairdresser(Arrays.asList(task1.getId(), task2.getId()));

        assertEquals(270.0, price); // 90% of total
    }
}
