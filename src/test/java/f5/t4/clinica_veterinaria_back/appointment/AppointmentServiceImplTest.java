package f5.t4.clinica_veterinaria_back.appointment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import f5.t4.clinica_veterinaria_back.appointment.dtos.AppointmentRequestDTO;
import f5.t4.clinica_veterinaria_back.appointment.dtos.AppointmentResponseDTO;
import f5.t4.clinica_veterinaria_back.appointment.enums.AppointmentStatus;
import f5.t4.clinica_veterinaria_back.appointment.exceptions.AppointmentNotFoundException;
import f5.t4.clinica_veterinaria_back.email.EmailService;
import f5.t4.clinica_veterinaria_back.patient.PatientEntity;
import f5.t4.clinica_veterinaria_back.patient.PatientRepository;
import f5.t4.clinica_veterinaria_back.patient.exceptions.PatientException;
import f5.t4.clinica_veterinaria_back.user.UserEntity;
import f5.t4.clinica_veterinaria_back.user.UserRepository;
import f5.t4.clinica_veterinaria_back.user.exceptions.UserNotFoundException;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AppointmentMapper appointmentMapper;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private AppointmentEntity appointmentEntity;
    private AppointmentRequestDTO appointmentRequestDTO;
    private AppointmentResponseDTO appointmentResponseDTO;
    private PatientEntity patientEntity;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        LocalDateTime appointmentTime = LocalDateTime.of(2024, 3, 15, 10, 30);

        // Entidades
        patientEntity = new PatientEntity();
        patientEntity.setId_patient(1L);
        patientEntity.setName("Firulais");

        userEntity = new UserEntity();
        userEntity.setId_user(1L);
        userEntity.setEmail("user@test.com");

        appointmentEntity = new AppointmentEntity();
        appointmentEntity.setId_appointment(1L);
        appointmentEntity.setAppointmentDatetime(appointmentTime);
        appointmentEntity.setType(true);
        appointmentEntity.setReason("Consulta veterinaria");
        appointmentEntity.setStatus(AppointmentStatus.PENDIENTE);
        appointmentEntity.setPatient(patientEntity);
        appointmentEntity.setUser(userEntity);

        // DTOs
        appointmentRequestDTO = new AppointmentRequestDTO(
                appointmentTime,
                true,
                "Consulta veterinaria",
                AppointmentStatus.PENDIENTE,
                1L,
                1L
        );

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
    void getEntities_ShouldReturnAllAppointments() {
        List<AppointmentEntity> entities = Arrays.asList(appointmentEntity);
        when(appointmentRepository.findAll()).thenReturn(entities);
        when(appointmentMapper.toDTO(appointmentEntity)).thenReturn(appointmentResponseDTO);

        List<AppointmentResponseDTO> result = appointmentService.getEntities();

        assertThat(result, hasSize(1));
        assertThat(result.get(0), is(appointmentResponseDTO));
        verify(appointmentRepository).findAll();
        verify(appointmentMapper).toDTO(appointmentEntity);
    }

    @Test
    void createEntity_WithValidData_ShouldCreateAppointment() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patientEntity));
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(appointmentMapper.toEntity(appointmentRequestDTO)).thenReturn(appointmentEntity);
        when(appointmentRepository.save(appointmentEntity)).thenReturn(appointmentEntity);
        when(appointmentMapper.toDTO(appointmentEntity)).thenReturn(appointmentResponseDTO);
        
        // Mock para la lógica de límite de citas diarias
        when(appointmentRepository.countAppointmentsByDay(any(), any())).thenReturn(5L);

        AppointmentResponseDTO result = appointmentService.createEntity(appointmentRequestDTO);

        assertThat(result, is(appointmentResponseDTO));
        verify(patientRepository).findById(1L);
        verify(userRepository).findById(1L);
        verify(appointmentRepository).save(appointmentEntity);
        verify(emailService).sendAppointmentConfirmation(appointmentResponseDTO);
    }

    @Test
    void createEntity_WithNullPatientId_ShouldThrowPatientException() {
        AppointmentRequestDTO invalidRequest = new AppointmentRequestDTO(
                LocalDateTime.now(), true, "Test", AppointmentStatus.PENDIENTE, null, 1L
        );

        PatientException exception = assertThrows(PatientException.class, 
                () -> appointmentService.createEntity(invalidRequest));
        
        assertThat(exception.getMessage(), containsString("Patient ID cannot be null"));
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void createEntity_WithNullUserId_ShouldThrowUserNotFoundException() {
        AppointmentRequestDTO invalidRequest = new AppointmentRequestDTO(
                LocalDateTime.now(), true, "Test", AppointmentStatus.PENDIENTE, 1L, null
        );

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, 
                () -> appointmentService.createEntity(invalidRequest));
        
        assertThat(exception.getMessage(), containsString("User ID cannot be null"));
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void createEntity_WithNullAppointmentDateTime_ShouldThrowIllegalArgumentException() {
        AppointmentRequestDTO invalidRequest = new AppointmentRequestDTO(
                null, true, "Test", AppointmentStatus.PENDIENTE, 1L, 1L
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> appointmentService.createEntity(invalidRequest));
        
        assertThat(exception.getMessage(), containsString("Appointment datetime cannot be null"));
    }

    @Test
    void createEntity_WhenDailyLimitReached_ShouldThrowRuntimeException() {
        when(appointmentRepository.countAppointmentsByDay(any(), any())).thenReturn(10L);

        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> appointmentService.createEntity(appointmentRequestDTO));
        
        assertThat(exception.getMessage(), containsString("El límite de 10 citas diarias"));
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void createEntity_WithNonExistentPatient_ShouldThrowPatientException() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());
        when(appointmentRepository.countAppointmentsByDay(any(), any())).thenReturn(5L);

        PatientException exception = assertThrows(PatientException.class, 
                () -> appointmentService.createEntity(appointmentRequestDTO));
        
        assertThat(exception.getMessage(), containsString("Paciente no encontrado con id: 1"));
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void createEntity_WithNonExistentUser_ShouldThrowUserNotFoundException() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patientEntity));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(appointmentRepository.countAppointmentsByDay(any(), any())).thenReturn(5L);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, 
                () -> appointmentService.createEntity(appointmentRequestDTO));
        
        assertThat(exception.getMessage(), containsString("Usuario no encontrado con id: 1"));
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void getByID_WithExistingId_ShouldReturnAppointment() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointmentEntity));
        when(appointmentMapper.toDTO(appointmentEntity)).thenReturn(appointmentResponseDTO);

        AppointmentResponseDTO result = appointmentService.getByID(1L);

        assertThat(result, is(appointmentResponseDTO));
        verify(appointmentRepository).findById(1L);
        verify(appointmentMapper).toDTO(appointmentEntity);
    }

    @Test
    void getByID_WithNonExistentId_ShouldThrowAppointmentNotFoundException() {
        when(appointmentRepository.findById(999L)).thenReturn(Optional.empty());

        AppointmentNotFoundException exception = assertThrows(AppointmentNotFoundException.class, 
                () -> appointmentService.getByID(999L));
        
        assertThat(exception.getMessage(), containsString("Cita no encontrada con id: 999"));
    }

    @Test
    void updateEntity_WithValidData_ShouldUpdateAppointment() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointmentEntity));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patientEntity));
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(appointmentRepository.save(appointmentEntity)).thenReturn(appointmentEntity);
        when(appointmentMapper.toDTO(appointmentEntity)).thenReturn(appointmentResponseDTO);

        AppointmentResponseDTO result = appointmentService.updateEntity(1L, appointmentRequestDTO);

        assertThat(result, is(appointmentResponseDTO));
        verify(appointmentMapper).updateEntityFromDTO(appointmentEntity, appointmentRequestDTO);
        verify(appointmentRepository).save(appointmentEntity);
        verify(emailService).sendAppointmentUpdate(appointmentResponseDTO);
    }

    @Test
    void updateEntity_WithNonExistentId_ShouldThrowAppointmentNotFoundException() {
        when(appointmentRepository.findById(999L)).thenReturn(Optional.empty());

        AppointmentNotFoundException exception = assertThrows(AppointmentNotFoundException.class, 
                () -> appointmentService.updateEntity(999L, appointmentRequestDTO));
        
        assertThat(exception.getMessage(), containsString("Cita no encontrada con id: 999"));
    }

    @Test
    void deleteEntity_WithExistingId_ShouldDeleteAppointment() {
        when(appointmentRepository.existsById(1L)).thenReturn(true);

        appointmentService.deleteEntity(1L);

        verify(appointmentRepository).deleteById(1L);
    }

    @Test
    void deleteEntity_WithNonExistentId_ShouldThrowAppointmentNotFoundException() {
        when(appointmentRepository.existsById(999L)).thenReturn(false);

        AppointmentNotFoundException exception = assertThrows(AppointmentNotFoundException.class, 
                () -> appointmentService.deleteEntity(999L));
        
        assertThat(exception.getMessage(), containsString("Cita no encontrada con id: 999"));
    }

    @Test
    void getAppointmentsByPatient_ShouldReturnPatientAppointments() {
        List<AppointmentEntity> entities = Arrays.asList(appointmentEntity);
        when(appointmentRepository.findByPatient_IdPatient(1L)).thenReturn(entities);
        when(appointmentMapper.toDTO(appointmentEntity)).thenReturn(appointmentResponseDTO);

        List<AppointmentResponseDTO> result = appointmentService.getAppointmentsByPatient(1L);

        assertThat(result, hasSize(1));
        assertThat(result.get(0), is(appointmentResponseDTO));
        verify(appointmentRepository).findByPatient_IdPatient(1L);
    }

    @Test
    void getAppointmentsByUser_ShouldReturnUserAppointments() {
        List<AppointmentEntity> entities = Arrays.asList(appointmentEntity);
        when(appointmentRepository.findByUser_IdUser(1L)).thenReturn(entities);
        when(appointmentMapper.toDTO(appointmentEntity)).thenReturn(appointmentResponseDTO);

        List<AppointmentResponseDTO> result = appointmentService.getAppointmentsByUser(1L);

        assertThat(result, hasSize(1));
        assertThat(result.get(0), is(appointmentResponseDTO));
        verify(appointmentRepository).findByUser_IdUser(1L);
    }

    @Test
    void getAppointmentsByStatus_ShouldReturnAppointmentsByStatus() {
        List<AppointmentEntity> entities = Arrays.asList(appointmentEntity);
        when(appointmentRepository.findByStatus(AppointmentStatus.PENDIENTE)).thenReturn(entities);
        when(appointmentMapper.toDTO(appointmentEntity)).thenReturn(appointmentResponseDTO);

        List<AppointmentResponseDTO> result = appointmentService.getAppointmentsByStatus(AppointmentStatus.PENDIENTE);

        assertThat(result, hasSize(1));
        assertThat(result.get(0), is(appointmentResponseDTO));
        verify(appointmentRepository).findByStatus(AppointmentStatus.PENDIENTE);
    }

    @Test
    void getAppointmentsByType_ShouldReturnAppointmentsByType() {
        List<AppointmentEntity> entities = Arrays.asList(appointmentEntity);
        when(appointmentRepository.findByType(true)).thenReturn(entities);
        when(appointmentMapper.toDTO(appointmentEntity)).thenReturn(appointmentResponseDTO);

        List<AppointmentResponseDTO> result = appointmentService.getAppointmentsByType(true);

        assertThat(result, hasSize(1));
        assertThat(result.get(0), is(appointmentResponseDTO));
        verify(appointmentRepository).findByType(true);
    }

    @Test
    void getAppointmentsByDateRange_ShouldReturnAppointmentsInRange() {
        LocalDateTime startDate = LocalDateTime.of(2024, 3, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 3, 31, 23, 59);
        
        List<AppointmentEntity> entities = Arrays.asList(appointmentEntity);
        when(appointmentRepository.findByAppointmentDatetimeBetween(startDate, endDate)).thenReturn(entities);
        when(appointmentMapper.toDTO(appointmentEntity)).thenReturn(appointmentResponseDTO);

        List<AppointmentResponseDTO> result = appointmentService.getAppointmentsByDateRange(startDate, endDate);

        assertThat(result, hasSize(1));
        assertThat(result.get(0), is(appointmentResponseDTO));
        verify(appointmentRepository).findByAppointmentDatetimeBetween(startDate, endDate);
    }

    @Test
    void getUpcomingAppointmentsByPatient_ShouldReturnUpcomingAppointments() {
        List<AppointmentEntity> entities = Arrays.asList(appointmentEntity);
        when(appointmentRepository.findUpcomingAppointmentsByPatient(eq(1L), any(LocalDateTime.class)))
                .thenReturn(entities);
        when(appointmentMapper.toDTO(appointmentEntity)).thenReturn(appointmentResponseDTO);

        List<AppointmentResponseDTO> result = appointmentService.getUpcomingAppointmentsByPatient(1L);

        assertThat(result, hasSize(1));
        assertThat(result.get(0), is(appointmentResponseDTO));
        verify(appointmentRepository).findUpcomingAppointmentsByPatient(eq(1L), any(LocalDateTime.class));
    }

    @Test
    void getUpcomingAppointmentsByUser_ShouldReturnUpcomingAppointments() {
        List<AppointmentEntity> entities = Arrays.asList(appointmentEntity);
        when(appointmentRepository.findUpcomingAppointmentsByUser(eq(1L), any(LocalDateTime.class)))
                .thenReturn(entities);
        when(appointmentMapper.toDTO(appointmentEntity)).thenReturn(appointmentResponseDTO);

        List<AppointmentResponseDTO> result = appointmentService.getUpcomingAppointmentsByUser(1L);

        assertThat(result, hasSize(1));
        assertThat(result.get(0), is(appointmentResponseDTO));
        verify(appointmentRepository).findUpcomingAppointmentsByUser(eq(1L), any(LocalDateTime.class));
    }
}