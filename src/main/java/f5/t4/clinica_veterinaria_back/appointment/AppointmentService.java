package f5.t4.clinica_veterinaria_back.appointment;

import java.time.LocalDateTime;
import java.util.List;

import f5.t4.clinica_veterinaria_back.appointment.dtos.AppointmentRequestDTO;
import f5.t4.clinica_veterinaria_back.appointment.dtos.AppointmentResponseDTO;
import f5.t4.clinica_veterinaria_back.implementations.IService;

public interface AppointmentService extends IService<AppointmentResponseDTO, AppointmentRequestDTO> {
    
    List<AppointmentResponseDTO> getAppointmentsByPatient(Long patientId);
    
    List<AppointmentResponseDTO> getAppointmentsByUser(Long userId);
    
    List<AppointmentResponseDTO> getAppointmentsByStatus(String status);
    
    List<AppointmentResponseDTO> getAppointmentsByType(Boolean type);
    
    List<AppointmentResponseDTO> getAppointmentsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    List<AppointmentResponseDTO> getUpcomingAppointmentsByPatient(Long patientId);
    
    List<AppointmentResponseDTO> getUpcomingAppointmentsByUser(Long userId);
}