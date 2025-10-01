package f5.t4.clinica_veterinaria_back.appointment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import f5.t4.clinica_veterinaria_back.appointment.enums.AppointmentStatus;
import f5.t4.clinica_veterinaria_back.patient.PatientEntity;
import f5.t4.clinica_veterinaria_back.user.UserEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AppointmentEntityTest {

    @Mock
    private PatientEntity patient;

    @Mock
    private UserEntity user;

    private AppointmentEntity appointment;

    @BeforeEach
    void setUp() {
        appointment = new AppointmentEntity(
                1L,
                LocalDateTime.of(2025, 1, 10, 15, 0),
                true,
                "Vacunación anual",
                AppointmentStatus.PENDIENTE,
                patient,
                user
        );
    }

    @Test
    void testAppointmentFields_areSetCorrectly() {
        assertThat(appointment.getId_appointment(), is(1L));
        assertThat(appointment.getAppointmentDatetime(), is(LocalDateTime.of(2025, 1, 10, 15, 0)));
        assertThat(appointment.getType(), is(true));
        assertThat(appointment.getReason(), is("Vacunación anual"));
        assertThat(appointment.getStatus(), is(AppointmentStatus.PENDIENTE));
    }

    @Test
    void testRelations_withMocks() {
        when(patient.getId_patient()).thenReturn(100L);
        when(user.getId_user()).thenReturn(200L);

        assertThat(appointment.getPatient(), is(patient));
        assertThat(appointment.getUser(), is(user));

        assertThat(appointment.getPatient().getId_patient(), is(100L));
        assertThat(appointment.getUser().getId_user(), is(200L));

        verify(patient).getId_patient();
        verify(user).getId_user();
    }

    @Test
    void testSetters_updateValues() {
        appointment.setReason("Chequeo general");
        appointment.setStatus(AppointmentStatus.ATENDIDA);

        assertThat(appointment.getReason(), containsString("Chequeo"));
        assertThat(appointment.getStatus(), is(AppointmentStatus.ATENDIDA));
    }
}
