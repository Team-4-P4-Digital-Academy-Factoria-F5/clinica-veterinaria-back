package f5.t4.clinica_veterinaria_back.appointment;

import org.springframework.stereotype.Component;

import f5.t4.clinica_veterinaria_back.appointment.dtos.AppointmentRequestDTO;
import f5.t4.clinica_veterinaria_back.appointment.dtos.AppointmentResponseDTO;

@Component
public class AppointmentMapper {

    public AppointmentEntity toEntity(AppointmentRequestDTO dto) {
        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setAppointmentDatetime(dto.appointmentDatetime());
        appointment.setType(dto.type());
        appointment.setReason(dto.reason());
        appointment.setStatus(dto.status());
        return appointment;
    }

    public AppointmentResponseDTO toDTO(AppointmentEntity entity) {
        return new AppointmentResponseDTO(
            entity.getId_appointment(),
            entity.getAppointmentDatetime(),
            entity.getType(),
            entity.getReason(),
            entity.getStatus(),
            entity.getPatient() != null ? entity.getPatient().getId_patient() : null,
            entity.getPatient() != null ? entity.getPatient().getName() : null,
            entity.getUser() != null ? entity.getUser().getId_user() : null,
            entity.getUser() != null ? entity.getUser().getEmail() : null
        );
    }

    public void updateEntityFromDTO(AppointmentEntity entity, AppointmentRequestDTO dto) {
        entity.setAppointmentDatetime(dto.appointmentDatetime());
        entity.setType(dto.type());
        entity.setReason(dto.reason());
        entity.setStatus(dto.status());
    }
}