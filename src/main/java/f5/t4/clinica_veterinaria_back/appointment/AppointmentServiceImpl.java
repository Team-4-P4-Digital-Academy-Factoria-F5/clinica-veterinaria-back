package f5.t4.clinica_veterinaria_back.appointment;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import f5.t4.clinica_veterinaria_back.appointment.dtos.AppointmentRequestDTO;
import f5.t4.clinica_veterinaria_back.appointment.dtos.AppointmentResponseDTO;
import f5.t4.clinica_veterinaria_back.appointment.exceptions.AppointmentNotFoundException;
import f5.t4.clinica_veterinaria_back.patient.PatientEntity;
import f5.t4.clinica_veterinaria_back.patient.PatientRepository;
import f5.t4.clinica_veterinaria_back.patient.exceptions.PatientException;
import f5.t4.clinica_veterinaria_back.user.UserEntity;
import f5.t4.clinica_veterinaria_back.user.UserRepository;
import f5.t4.clinica_veterinaria_back.user.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final AppointmentMapper appointmentMapper;

    @Override
    public List<AppointmentResponseDTO> getEntities() {
        return appointmentRepository.findAll().stream()
                .map(appointmentMapper::toDTO)
                .toList();
    }

    @Override
    public AppointmentResponseDTO createEntity(AppointmentRequestDTO dto) {
        if (dto.patientId() == null) {
            throw new PatientException("Patient ID cannot be null");
        }
        if (dto.userId() == null) {
            throw new UserNotFoundException("User ID cannot be null");
        }

        PatientEntity patient = patientRepository.findById(dto.patientId())
                .orElseThrow(() -> new PatientException("Paciente no encontrado con id: " + dto.patientId()));

        UserEntity user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con id: " + dto.userId()));

        AppointmentEntity appointment = appointmentMapper.toEntity(dto);
        appointment.setPatient(patient);
        appointment.setUser(user);

        AppointmentEntity savedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDTO(savedAppointment);
    }

    @Override
    public AppointmentResponseDTO getByID(Long id) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Cita no encontrada con id: " + id));
        return appointmentMapper.toDTO(appointment);
    }

    @Override
    public AppointmentResponseDTO updateEntity(Long id, AppointmentRequestDTO dto) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Cita no encontrada con id: " + id));

        if (dto.patientId() == null) {
            throw new PatientException("Patient ID cannot be null");
        }
        if (dto.userId() == null) {
            throw new UserNotFoundException("User ID cannot be null");
        }

        PatientEntity patient = patientRepository.findById(dto.patientId())
                .orElseThrow(() -> new PatientException("Paciente no encontrado con id: " + dto.patientId()));

        UserEntity user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con id: " + dto.userId()));

        appointmentMapper.updateEntityFromDTO(appointment, dto);
        appointment.setPatient(patient);
        appointment.setUser(user);

        AppointmentEntity updatedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDTO(updatedAppointment);
    }

    @Override
    public void deleteEntity(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new AppointmentNotFoundException("Cita no encontrada con id: " + id);
        }
        appointmentRepository.deleteById(id);
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatient_IdPatient(patientId).stream()
                .map(appointmentMapper::toDTO)
                .toList();
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByUser(Long userId) {
        return appointmentRepository.findByUser_IdUser(userId).stream()
                .map(appointmentMapper::toDTO)
                .toList();
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByStatus(String status) {
        return appointmentRepository.findByStatus(status).stream()
                .map(appointmentMapper::toDTO)
                .toList();
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByType(Boolean type) {
        return appointmentRepository.findByType(type).stream()
                .map(appointmentMapper::toDTO)
                .toList();
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return appointmentRepository.findByAppointmentDatetimeBetween(startDate, endDate).stream()
                .map(appointmentMapper::toDTO)
                .toList();
    }

    @Override
    public List<AppointmentResponseDTO> getUpcomingAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findUpcomingAppointmentsByPatient(patientId, LocalDateTime.now()).stream()
                .map(appointmentMapper::toDTO)
                .toList();
    }

    @Override
    public List<AppointmentResponseDTO> getUpcomingAppointmentsByUser(Long userId) {
        return appointmentRepository.findUpcomingAppointmentsByUser(userId, LocalDateTime.now()).stream()
                .map(appointmentMapper::toDTO)
                .toList();
    }
}