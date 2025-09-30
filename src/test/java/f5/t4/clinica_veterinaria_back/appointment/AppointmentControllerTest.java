package f5.t4.clinica_veterinaria_back.appointment;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import f5.t4.clinica_veterinaria_back.appointment.dtos.AppointmentRequestDTO;
import f5.t4.clinica_veterinaria_back.appointment.dtos.AppointmentResponseDTO;
import f5.t4.clinica_veterinaria_back.appointment.enums.AppointmentStatus;
import f5.t4.clinica_veterinaria_back.role.RoleEntity;
import f5.t4.clinica_veterinaria_back.security.JpaUserDetailsService;
import f5.t4.clinica_veterinaria_back.user.UserEntity;
import f5.t4.clinica_veterinaria_back.user.UserRepository;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AppointmentService appointmentService;

    @MockitoBean
    private UserRepository userRepository;
    
    @MockitoBean
    private JpaUserDetailsService jpaUserDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private AppointmentRequestDTO appointmentRequestDTO;
    private AppointmentResponseDTO appointmentResponseDTO;
    private UserEntity userEntity;
    private UserEntity adminEntity;

    @BeforeEach
    void setUp() {
        LocalDateTime appointmentTime = LocalDateTime.of(2024, 3, 15, 10, 30);
        
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

        RoleEntity userRole = new RoleEntity();
        userRole.setId_role(1L);
        userRole.setName("ROLE_USER");
        
        userEntity = new UserEntity();
        userEntity.setId_user(1L);
        userEntity.setEmail("user@test.com");
        userEntity.setRoles(Set.of(userRole));

        RoleEntity adminRole = new RoleEntity();
        adminRole.setId_role(2L);
        adminRole.setName("ROLE_ADMIN");
        
        adminEntity = new UserEntity();
        adminEntity.setId_user(2L);
        adminEntity.setEmail("admin@test.com");
        adminEntity.setRoles(Set.of(adminRole));
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void getAllAppointments_AsAdmin_ShouldReturnAllAppointments() throws Exception {
        List<AppointmentResponseDTO> appointments = Arrays.asList(appointmentResponseDTO);
        when(appointmentService.getEntities()).thenReturn(appointments);

        mockMvc.perform(get("/api/v1/appointments").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id_appointment", is(1)))
                .andExpect(jsonPath("$[0].patientName", is("Firulais")))
                .andExpect(jsonPath("$[0].userEmail", is("user@test.com")));

        verify(appointmentService).getEntities();
    }

    @Test
    @WithMockUser(username = "user@test.com", roles = {"USER"})
    void createAppointment_WithValidData_ShouldReturnCreatedAppointment() throws Exception {
        when(appointmentService.createEntity(any(AppointmentRequestDTO.class)))
                .thenReturn(appointmentResponseDTO);

        mockMvc.perform(post("/api/v1/appointments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id_appointment", is(1)))
                .andExpect(jsonPath("$.patientName", is("Firulais")))
                .andExpect(jsonPath("$.status", is("PENDIENTE")));

        verify(appointmentService).createEntity(any(AppointmentRequestDTO.class));
    }

    @Test
    @WithMockUser(username = "user@test.com", roles = {"USER"})
    void createAppointment_WithNullDateTime_ShouldReturnBadRequest() throws Exception {
        AppointmentRequestDTO invalidRequest = new AppointmentRequestDTO(
                null, true, "Consulta", AppointmentStatus.PENDIENTE, 1L, 1L
        );

        mockMvc.perform(post("/api/v1/appointments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(appointmentService, never()).createEntity(any());
    }

    @Test
    @WithMockUser(username = "user@test.com", roles = {"USER"})
    void getAppointmentById_AsOwner_ShouldReturnAppointment() throws Exception {
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(userEntity));
        when(appointmentService.getByID(1L)).thenReturn(appointmentResponseDTO);

        mockMvc.perform(get("/api/v1/appointments/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_appointment", is(1)))
                .andExpect(jsonPath("$.userEmail", is("user@test.com")));

        verify(appointmentService).getByID(1L);
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void getAppointmentById_AsAdmin_ShouldReturnAppointment() throws Exception {
        when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(adminEntity));
        when(appointmentService.getByID(1L)).thenReturn(appointmentResponseDTO);

        mockMvc.perform(get("/api/v1/appointments/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_appointment", is(1)));

        verify(appointmentService).getByID(1L);
    }

    @Test
    @WithMockUser(username = "other@test.com", roles = {"USER"})
    void getAppointmentById_AsNonOwnerNonAdmin_ShouldThrowAccessDenied() throws Exception {
        UserEntity otherUser = new UserEntity();
        otherUser.setId_user(999L);
        otherUser.setEmail("other@test.com");
        RoleEntity userRole = new RoleEntity();
        userRole.setName("ROLE_USER");
        otherUser.setRoles(Set.of(userRole));

        when(userRepository.findByEmail("other@test.com")).thenReturn(Optional.of(otherUser));
        when(appointmentService.getByID(1L)).thenReturn(appointmentResponseDTO);

        mockMvc.perform(get("/api/v1/appointments/1").with(csrf()))
                .andExpect(status().isForbidden());

        verify(appointmentService).getByID(1L);
    }

    @Test
    @WithMockUser(username = "user@test.com", roles = {"USER"})
    void updateAppointment_AsOwner_ShouldReturnUpdatedAppointment() throws Exception {
        AppointmentResponseDTO updatedResponse = new AppointmentResponseDTO(
                1L, appointmentRequestDTO.appointmentDatetime(), true, "Updated reason",
                AppointmentStatus.ATENDIDA, 1L, "Firulais", 1L, "user@test.com"
        );

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(userEntity));
        when(appointmentService.getByID(1L)).thenReturn(appointmentResponseDTO);
        when(appointmentService.updateEntity(eq(1L), any(AppointmentRequestDTO.class)))
                .thenReturn(updatedResponse);

        mockMvc.perform(put("/api/v1/appointments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("ATENDIDA")))
                .andExpect(jsonPath("$.reason", is("Updated reason")));

        verify(appointmentService).updateEntity(eq(1L), any(AppointmentRequestDTO.class));
    }

    @Test
    @WithMockUser(username = "user@test.com", roles = {"USER"})
    void deleteAppointment_AsOwner_ShouldReturnNoContent() throws Exception {
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(userEntity));
        when(appointmentService.getByID(1L)).thenReturn(appointmentResponseDTO);
        doNothing().when(appointmentService).deleteEntity(1L);

        mockMvc.perform(delete("/api/v1/appointments/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(appointmentService).deleteEntity(1L);
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void getAppointmentsByPatient_AsAdmin_ShouldReturnPatientAppointments() throws Exception {
        List<AppointmentResponseDTO> appointments = Arrays.asList(appointmentResponseDTO);
        when(appointmentService.getAppointmentsByPatient(1L)).thenReturn(appointments);

        mockMvc.perform(get("/api/v1/appointments/patient/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].patientId", is(1)));

        verify(appointmentService).getAppointmentsByPatient(1L);
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void getAppointmentsByStatus_ShouldReturnAppointmentsByStatus() throws Exception {
        List<AppointmentResponseDTO> appointments = Arrays.asList(appointmentResponseDTO);
        when(appointmentService.getAppointmentsByStatus(AppointmentStatus.PENDIENTE))
                .thenReturn(appointments);

        mockMvc.perform(get("/api/v1/appointments/status/PENDIENTE").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status", is("PENDIENTE")));

        verify(appointmentService).getAppointmentsByStatus(AppointmentStatus.PENDIENTE);
    }

    @Test
    @WithMockUser(username = "user@test.com", roles = {"USER"})
    void getMyAppointments_ShouldReturnUserAppointments() throws Exception {
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(userEntity));
        when(appointmentService.getAppointmentsByUser(1L))
                .thenReturn(Arrays.asList(appointmentResponseDTO));

        mockMvc.perform(get("/api/v1/appointments/my-appointments").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userId", is(1)));

        verify(appointmentService).getAppointmentsByUser(1L);
    }

    @Test
    @WithMockUser(username = "user@test.com", roles = {"USER"})
    void getAvailableStatuses_ShouldReturnAllStatuses() throws Exception {
        mockMvc.perform(get("/api/v1/appointments/status/available").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$", containsInAnyOrder("PENDIENTE", "ATENDIDA", "PASADA")));
    }

}