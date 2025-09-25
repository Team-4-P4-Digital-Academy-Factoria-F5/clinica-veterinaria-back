package f5.t4.clinica_veterinaria_back.appointment.dtos;

import java.time.LocalDateTime;

public record AppointmentRequestDTO(
    LocalDateTime appointmentDatetime,
    Boolean type,
    String reason,
    String status,
    Long patientId,
    Long userId
) {}