package f5.t4.clinica_veterinaria_back.appointment;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import f5.t4.clinica_veterinaria_back.appointment.dtos.AppointmentRequestDTO;
import f5.t4.clinica_veterinaria_back.appointment.dtos.AppointmentResponseDTO;
import f5.t4.clinica_veterinaria_back.appointment.enums.AppointmentStatus;
import f5.t4.clinica_veterinaria_back.user.UserEntity;
import f5.t4.clinica_veterinaria_back.user.UserRepository;
import f5.t4.clinica_veterinaria_back.user.exceptions.UserAccessDeniedException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "${api-endpoint}/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserRepository userRepository;

    @GetMapping("")
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getEntities());
    }

    @PostMapping("")
    public ResponseEntity<AppointmentResponseDTO> createAppointment(@RequestBody AppointmentRequestDTO dto) {
        if (dto.appointmentDatetime() == null) {
            return ResponseEntity.badRequest().build();
        }

        AppointmentResponseDTO appointmentStored = appointmentService.createEntity(dto);
        
        if (appointmentStored == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(201).body(appointmentStored);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> getAppointmentById(
            @PathVariable Long id, 
            Principal principal) {
        
        UserEntity currentUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        AppointmentResponseDTO appointment = appointmentService.getByID(id);

        // Verificar si el usuario puede acceder a esta cita
        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(r -> r.getName().equals("ROLE_ADMIN"));
        
        if (!isAdmin && !currentUser.getId_user().equals(appointment.userId())) {
            throw new UserAccessDeniedException("No puedes acceder a esta cita");
        }

        return ResponseEntity.ok(appointment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> updateAppointment(
            @PathVariable Long id,
            @RequestBody AppointmentRequestDTO dto,
            Principal principal) {

        UserEntity currentUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(r -> r.getName().equals("ROLE_ADMIN"));

        // Obtener la cita actual para verificar permisos
        AppointmentResponseDTO currentAppointment = appointmentService.getByID(id);
        
        if (!isAdmin && !currentUser.getId_user().equals(currentAppointment.userId())) {
            throw new UserAccessDeniedException("No puedes editar esta cita");
        }

        AppointmentResponseDTO updated = appointmentService.updateEntity(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(
            @PathVariable Long id, 
            Principal principal) {
        
        UserEntity currentUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(r -> r.getName().equals("ROLE_ADMIN"));

        // Obtener la cita actual para verificar permisos
        AppointmentResponseDTO currentAppointment = appointmentService.getByID(id);
        
        if (!isAdmin && !currentUser.getId_user().equals(currentAppointment.userId())) {
            throw new UserAccessDeniedException("No puedes borrar esta cita");
        }

        appointmentService.deleteEntity(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(patientId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByUser(userId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByStatus(@PathVariable AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByStatus(status));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByType(@PathVariable Boolean type) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByType(type));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDateRange(startDate, endDate));
    }

    @GetMapping("/upcoming/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getUpcomingAppointmentsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.getUpcomingAppointmentsByPatient(patientId));
    }

    @GetMapping("/upcoming/user/{userId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getUpcomingAppointmentsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(appointmentService.getUpcomingAppointmentsByUser(userId));
    }

    @GetMapping("/my-appointments")
    public ResponseEntity<List<AppointmentResponseDTO>> getMyAppointments(Principal principal) {
        UserEntity currentUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        
        return ResponseEntity.ok(appointmentService.getAppointmentsByUser(currentUser.getId_user()));
    }

    @GetMapping("/my-upcoming-appointments")
    public ResponseEntity<List<AppointmentResponseDTO>> getMyUpcomingAppointments(Principal principal) {
        UserEntity currentUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        
        return ResponseEntity.ok(appointmentService.getUpcomingAppointmentsByUser(currentUser.getId_user()));
    }

    @GetMapping("/status/available")
    public ResponseEntity<List<String>> getAvailableStatuses() {
        List<String> statuses = Arrays.stream(AppointmentStatus.values())
                .map(AppointmentStatus::name)
                .toList();
        return ResponseEntity.ok(statuses);
    }
}