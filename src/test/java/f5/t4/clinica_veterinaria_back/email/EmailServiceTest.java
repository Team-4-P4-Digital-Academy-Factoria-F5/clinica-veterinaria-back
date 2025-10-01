package f5.t4.clinica_veterinaria_back.email;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import f5.t4.clinica_veterinaria_back.appointment.dtos.AppointmentResponseDTO;
import f5.t4.clinica_veterinaria_back.appointment.enums.AppointmentStatus;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    private AppointmentResponseDTO appointmentResponseDTO;

    @BeforeEach
    void setUp() {
        LocalDateTime appointmentTime = LocalDateTime.of(2024, 3, 15, 10, 30);
        
        appointmentResponseDTO = new AppointmentResponseDTO(
                1L,
                appointmentTime,
                true,
                "Consulta veterinaria",
                AppointmentStatus.PENDIENTE,
                1L,
                "Firulais",
                1L,
                "user@test.com"
        );
    }

    @Test
    void sendAppointmentConfirmation_WithValidAppointment_ShouldSendEmail() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendAppointmentConfirmation(appointmentResponseDTO);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertThat(sentMessage.getTo(), arrayContaining("user@test.com"));
        assertThat(sentMessage.getSubject(), is("Confirmación de Cita - Clínica Veterinaria"));
        assertThat(sentMessage.getFrom(), is("noreply@clinicaveterinaria.com"));
        assertThat(sentMessage.getText(), containsString("Su cita ha sido confirmada"));
        assertThat(sentMessage.getText(), containsString("15/03/2024 a las 10:30"));
        assertThat(sentMessage.getText(), containsString("Firulais"));
        assertThat(sentMessage.getText(), containsString("Consulta veterinaria"));
        assertThat(sentMessage.getText(), containsString("Pendiente"));
        assertThat(sentMessage.getText(), containsString("#1"));
    }

    @Test
    void sendAppointmentConfirmation_WithNullPatientName_ShouldUseNA() {
        AppointmentResponseDTO appointmentWithNullPatient = new AppointmentResponseDTO(
                1L,
                appointmentResponseDTO.appointmentDatetime(),
                true,
                "Consulta veterinaria",
                AppointmentStatus.PENDIENTE,
                1L,
                null, // Nombre del paciente nulo
                1L,
                "user@test.com"
        );

        emailService.sendAppointmentConfirmation(appointmentWithNullPatient);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertThat(sentMessage.getText(), containsString("N/A"));
    }

    @Test
    void sendAppointmentConfirmation_WithNullReason_ShouldUseDefaultReason() {
        AppointmentResponseDTO appointmentWithNullReason = new AppointmentResponseDTO(
                1L,
                appointmentResponseDTO.appointmentDatetime(),
                true,
                null, // Razón nula
                AppointmentStatus.PENDIENTE,
                1L,
                "Firulais",
                1L,
                "user@test.com"
        );

        emailService.sendAppointmentConfirmation(appointmentWithNullReason);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertThat(sentMessage.getText(), containsString("Consulta general"));
    }

    @Test
    void sendAppointmentConfirmation_WhenMailSenderThrowsException_ShouldNotThrowException() {
        doThrow(new MailException("Mail server error") {})
                .when(mailSender).send(any(SimpleMailMessage.class));

        // No debería lanzar excepción
        emailService.sendAppointmentConfirmation(appointmentResponseDTO);

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendAppointmentUpdate_WithValidAppointment_ShouldSendEmail() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendAppointmentUpdate(appointmentResponseDTO);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertThat(sentMessage.getTo(), arrayContaining("user@test.com"));
        assertThat(sentMessage.getSubject(), is("Actualización de Cita - Clínica Veterinaria"));
        assertThat(sentMessage.getText(), containsString("Su cita ha sido actualizada"));
        assertThat(sentMessage.getText(), containsString("NUEVOS DETALLES"));
    }

    @Test
    void sendAppointmentUpdate_WhenMailSenderThrowsException_ShouldNotThrowException() {
        doThrow(new MailException("Mail server error") {})
                .when(mailSender).send(any(SimpleMailMessage.class));

        // No debería lanzar excepción
        emailService.sendAppointmentUpdate(appointmentResponseDTO);

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendAppointmentCancellation_WithValidAppointment_ShouldSendEmail() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendAppointmentCancellation(appointmentResponseDTO);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertThat(sentMessage.getTo(), arrayContaining("user@test.com"));
        assertThat(sentMessage.getSubject(), is("Cancelación de Cita - Clínica Veterinaria"));
        assertThat(sentMessage.getText(), containsString("Su cita ha sido cancelada"));
        assertThat(sentMessage.getText(), containsString("DETALLES DE LA CITA CANCELADA"));
        assertThat(sentMessage.getText(), containsString("15/03/2024 a las 10:30"));
        assertThat(sentMessage.getText(), containsString("Firulais"));
        assertThat(sentMessage.getText(), containsString("#1"));
    }

    @Test
    void sendAppointmentCancellation_WhenMailSenderThrowsException_ShouldNotThrowException() {
        doThrow(new MailException("Mail server error") {})
                .when(mailSender).send(any(SimpleMailMessage.class));

        // No debería lanzar excepción
        emailService.sendAppointmentCancellation(appointmentResponseDTO);

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendAppointmentUpdate_WithDifferentStatus_ShouldIncludeCorrectStatus() {
        AppointmentResponseDTO attendedAppointment = new AppointmentResponseDTO(
                1L,
                appointmentResponseDTO.appointmentDatetime(),
                true,
                "Consulta veterinaria",
                AppointmentStatus.ATENDIDA,
                1L,
                "Firulais",
                1L,
                "user@test.com"
        );

        emailService.sendAppointmentUpdate(attendedAppointment);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertThat(sentMessage.getText(), containsString("Atendida"));
    }

    @Test
    void sendAppointmentConfirmation_ShouldContainAllRequiredInformation() {
        emailService.sendAppointmentConfirmation(appointmentResponseDTO);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        String emailText = sentMessage.getText();
        
        // Verificar que contiene toda la información importante
        assertThat(emailText, containsString("DETALLES DE LA CITA:"));
        assertThat(emailText, containsString("Fecha y Hora:"));
        assertThat(emailText, containsString("Paciente:"));
        assertThat(emailText, containsString("Motivo:"));
        assertThat(emailText, containsString("Estado:"));
        assertThat(emailText, containsString("Número de Cita:"));
        assertThat(emailText, containsString("Por favor, llegue 10 minutos antes"));
        assertThat(emailText, containsString("Atentamente"));
        assertThat(emailText, containsString("Clínica Veterinaria"));
    }

    @Test
    void emailMessages_ShouldHaveDifferentContent() {
        ArgumentCaptor<SimpleMailMessage> confirmationCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        ArgumentCaptor<SimpleMailMessage> updateCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        ArgumentCaptor<SimpleMailMessage> cancellationCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        emailService.sendAppointmentConfirmation(appointmentResponseDTO);
        verify(mailSender).send(confirmationCaptor.capture());

        emailService.sendAppointmentUpdate(appointmentResponseDTO);
        verify(mailSender, times(2)).send(updateCaptor.capture());

        emailService.sendAppointmentCancellation(appointmentResponseDTO);
        verify(mailSender, times(3)).send(cancellationCaptor.capture());

        String confirmationText = confirmationCaptor.getValue().getText();
        String updateText = updateCaptor.getAllValues().get(1).getText();
        String cancellationText = cancellationCaptor.getAllValues().get(2).getText();

        // Verificar que los mensajes son diferentes
        assertThat(confirmationText, not(equalTo(updateText)));
        assertThat(confirmationText, not(equalTo(cancellationText)));
        assertThat(updateText, not(equalTo(cancellationText)));
    }
}