package f5.t4.clinica_veterinaria_back.appointment;

import f5.t4.clinica_veterinaria_back.appointment.dtos.AppointmentRequestDTO;
import f5.t4.clinica_veterinaria_back.appointment.dtos.AppointmentResponseDTO;
import f5.t4.clinica_veterinaria_back.appointment.enums.AppointmentStatus;
import f5.t4.clinica_veterinaria_back.patient.PatientEntity;
import f5.t4.clinica_veterinaria_back.user.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AppointmentMapperTest {

    private AppointmentMapper appointmentMapper;
    private LocalDateTime testTime;
    private UserEntity testUser;
    private PatientEntity testPatient;

    @BeforeEach
    void setUp() {
        appointmentMapper = new AppointmentMapper();
        testTime = LocalDateTime.of(2025, 10, 29, 10, 0);

        // Configuración de entidades base mínimas para el test
        testUser = UserEntity.builder().id_user(100L).email("vet@test.com").build();
        testPatient = new PatientEntity(); 
        testPatient.setId_patient(200L);
        testPatient.setName("Firulais");
        // Nota: Si PatientEntity tiene una relación con UserEntity (tutor), debes setearla.
        // testPatient.setTutor(testUser); 
    }

    // ------------------- toEntity -------------------

    @Test
    void toEntity_ShouldMapRequestDTOToAppointmentEntity() {
        // Arrange
        AppointmentRequestDTO requestDTO = new AppointmentRequestDTO(
            testTime, 
            true, 
            "Consulta anual", 
            AppointmentStatus.PENDIENTE, 
            testPatient.getId_patient(), 
            testUser.getId_user()
        );

        // Act
        AppointmentEntity entity = appointmentMapper.toEntity(requestDTO);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getAppointmentDatetime()).isEqualTo(testTime);
        assertThat(entity.getType()).isTrue();
        assertThat(entity.getReason()).isEqualTo("Consulta anual");
        assertThat(entity.getStatus()).isEqualTo(AppointmentStatus.PENDIENTE);
        // Las relaciones (Patient y User) se setean en el Service, no en el Mapper.
        assertThat(entity.getPatient()).isNull();
        assertThat(entity.getUser()).isNull(); 
    }

    // ------------------- toDTO -------------------

    @Test
    void toDTO_ShouldMapAppointmentEntityToResponseDTO() {
        // Arrange
        AppointmentEntity entity = new AppointmentEntity(
            50L, 
            testTime.plusDays(1), 
            false, 
            "Vacunación", 
            AppointmentStatus.ATENDIDA, 
            testPatient, // Entidad Patient (NO solo el ID)
            testUser     // Entidad User (NO solo el ID)
        );

        // Act
        AppointmentResponseDTO responseDTO = appointmentMapper.toDTO(entity);

        // Assert
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.id_appointment()).isEqualTo(50L);
        assertThat(responseDTO.appointmentDatetime()).isEqualTo(testTime.plusDays(1));
        assertThat(responseDTO.status()).isEqualTo(AppointmentStatus.ATENDIDA);
        
        // Assert de las relaciones
        assertThat(responseDTO.patientId()).isEqualTo(testPatient.getId_patient());
        assertThat(responseDTO.patientName()).isEqualTo(testPatient.getName());
        assertThat(responseDTO.userId()).isEqualTo(testUser.getId_user());
        assertThat(responseDTO.userEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    void toDTO_ShouldHandleNullRelationsGracefully() {
        // Arrange
        AppointmentEntity entity = new AppointmentEntity(
            51L, 
            testTime.plusHours(1), 
            false, 
            "Emergencia", 
            AppointmentStatus.PENDIENTE, 
            null, // Patient null
            null  // User null
        );

        // Act
        AppointmentResponseDTO responseDTO = appointmentMapper.toDTO(entity);

        // Assert
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.patientId()).isNull();
        assertThat(responseDTO.patientName()).isNull();
        assertThat(responseDTO.userId()).isNull();
        assertThat(responseDTO.userEmail()).isNull();
    }

    // ------------------- updateEntityFromDTO -------------------
    
    @Test
    void updateEntityFromDTO_ShouldUpdateEntityFields() {
        // Arrange
        AppointmentEntity entity = new AppointmentEntity(
            52L, 
            testTime, 
            true, 
            "Original reason", 
            AppointmentStatus.PENDIENTE, 
            testPatient, 
            testUser
        );
        AppointmentRequestDTO updateDTO = new AppointmentRequestDTO(
            testTime.plusHours(3), 
            false, 
            "Updated reason", 
            AppointmentStatus.ATENDIDA, 
            testPatient.getId_patient(), 
            testUser.getId_user()
        );

        // Act
        appointmentMapper.updateEntityFromDTO(entity, updateDTO);

        // Assert
        assertThat(entity.getId_appointment()).isEqualTo(52L); // ID should not change
        assertThat(entity.getAppointmentDatetime()).isEqualTo(testTime.plusHours(3));
        assertThat(entity.getType()).isFalse();
        assertThat(entity.getReason()).isEqualTo("Updated reason");
        assertThat(entity.getStatus()).isEqualTo(AppointmentStatus.ATENDIDA);
    }
}