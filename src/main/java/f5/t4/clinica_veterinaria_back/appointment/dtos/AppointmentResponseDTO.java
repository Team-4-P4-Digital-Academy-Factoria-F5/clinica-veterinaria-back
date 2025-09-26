package f5.t4.clinica_veterinaria_back.appointment.dtos;

import java.time.LocalDateTime;
import f5.t4.clinica_veterinaria_back.appointment.enums.AppointmentStatus;

public record AppointmentResponseDTO(
    Long id_appointment,
    LocalDateTime appointmentDatetime,
    Boolean type,
    String reason,
    AppointmentStatus status,
    Long patientId,
    String patientName,
    Long userId,
    String userEmail
) {}