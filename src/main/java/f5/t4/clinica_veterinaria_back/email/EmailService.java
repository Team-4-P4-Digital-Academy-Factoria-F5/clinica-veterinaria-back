package f5.t4.clinica_veterinaria_back.email;

import java.time.format.DateTimeFormatter;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import f5.t4.clinica_veterinaria_back.appointment.dtos.AppointmentResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendAppointmentConfirmation(AppointmentResponseDTO appointment) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            
            // Configurar destinatario
            message.setTo(appointment.userEmail());
            
            // Configurar asunto
            message.setSubject("Confirmación de Cita - Clínica Veterinaria");
            
            // Crear el contenido del email
            String emailContent = createAppointmentEmailContent(appointment);
            message.setText(emailContent);
            
            // Configurar remitente (opcional, se puede configurar en application.properties)
            message.setFrom("noreply@clinicaveterinaria.com");
            
            // Enviar el email
            mailSender.send(message);
            
            log.info("Email de confirmación enviado a: {}", appointment.userEmail());
            
        } catch (Exception e) {
            log.error("Error al enviar email de confirmación a {}: {}", 
                     appointment.userEmail(), e.getMessage(), e);
            // No lanzamos la excepción para que no falle la creación de la cita
            // si hay problemas con el email
        }
    }

    public void sendAppointmentUpdate(AppointmentResponseDTO appointment) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            
            message.setTo(appointment.userEmail());
            message.setSubject("Actualización de Cita - Clínica Veterinaria");
            
            String emailContent = createAppointmentUpdateEmailContent(appointment);
            message.setText(emailContent);
            
            message.setFrom("noreply@clinicaveterinaria.com");
            
            mailSender.send(message);
            
            log.info("Email de actualización enviado a: {}", appointment.userEmail());
            
        } catch (Exception e) {
            log.error("Error al enviar email de actualización a {}: {}", 
                     appointment.userEmail(), e.getMessage(), e);
        }
    }

    public void sendAppointmentCancellation(AppointmentResponseDTO appointment) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            
            message.setTo(appointment.userEmail());
            message.setSubject("Cancelación de Cita - Clínica Veterinaria");
            
            String emailContent = createAppointmentCancellationEmailContent(appointment);
            message.setText(emailContent);
            
            message.setFrom("noreply@clinicaveterinaria.com");
            
            mailSender.send(message);
            
            log.info("Email de cancelación enviado a: {}", appointment.userEmail());
            
        } catch (Exception e) {
            log.error("Error al enviar email de cancelación a {}: {}", 
                     appointment.userEmail(), e.getMessage(), e);
        }
    }

    private String createAppointmentEmailContent(AppointmentResponseDTO appointment) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'a las' HH:mm");
        
        return String.format("""
            Estimado/a cliente,
            
            Su cita ha sido confirmada.
            
            DETALLES DE LA CITA:
            • Fecha y Hora: %s
            • Paciente: %s
            • Motivo: %s
            • Estado: %s
            • Número de Cita: #%d
            
            Por favor, llegue 10 minutos antes de la hora programada.
            
            Si necesita modificar o cancelar su cita, póngase en contacto con nosotros.
            
            Atentamente,
            Clínica Veterinaria Margarita
            """, 
            appointment.appointmentDatetime().format(formatter),
            appointment.patientName() != null ? appointment.patientName() : "N/A",
            appointment.reason() != null ? appointment.reason() : "Consulta general",
            appointment.status().getDisplayName(),
            appointment.id_appointment()
        );
    }

    private String createAppointmentUpdateEmailContent(AppointmentResponseDTO appointment) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'a las' HH:mm");
        
        return String.format("""
            Estimado/a cliente,
            
            Su cita ha sido actualizada.
            
            NUEVOS DETALLES DE LA CITA:
            • Fecha y Hora: %s
            • Paciente: %s
            • Motivo: %s
            • Estado: %s
            • Número de Cita: #%d
            
            Por favor, tome nota de los cambios realizados.
            
            Atentamente,
            Clínica Veterinaria Margarita
            """, 
            appointment.appointmentDatetime().format(formatter),
            appointment.patientName() != null ? appointment.patientName() : "N/A",
            appointment.reason() != null ? appointment.reason() : "Consulta general",
            appointment.status().getDisplayName(),
            appointment.id_appointment()
        );
    }

    private String createAppointmentCancellationEmailContent(AppointmentResponseDTO appointment) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'a las' HH:mm");
        
        return String.format("""
            Estimado/a cliente,
            
            Su cita ha sido cancelada.
            
            DETALLES DE LA CITA CANCELADA:
            • Fecha y Hora: %s
            • Paciente: %s
            • Número de Cita: #%d
            
            Si desea reagendar, póngase en contacto con nosotros.
            
            Atentamente,
            Clínica Veterinaria
            Teléfono: [NÚMERO DE TELÉFONO]
            Email: info@clinicaveterinaria.com
            """, 
            appointment.appointmentDatetime().format(formatter),
            appointment.patientName() != null ? appointment.patientName() : "N/A",
            appointment.id_appointment()
        );
    }
}