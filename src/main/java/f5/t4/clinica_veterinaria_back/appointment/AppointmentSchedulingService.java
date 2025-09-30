// Archivo: AppointmentSchedulingService.java
package f5.t4.clinica_veterinaria_back.appointment;

import java.time.LocalDateTime;
import java.util.List;
import java.time.temporal.ChronoUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import f5.t4.clinica_veterinaria_back.appointment.enums.AppointmentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentSchedulingService {

    private final AppointmentRepository appointmentRepository;

    /**
     * Tarea programada para marcar citas PENDIENTES pasadas como PASADA.
     * Se ejecuta todos los días a las 00:00:00 (medianoche).
     */
    @Scheduled(cron = "0 0 0 * * *") // Se ejecuta a las 12:00 AM (medianoche) todos los días
    @Transactional
    public void markOverduePendingAppointmentsAsPassed() {
        log.info("Iniciando tarea programada para actualizar citas PENDIENTES a PASADA.");

        LocalDateTime now = LocalDateTime.now();
        
        // 1. Buscar citas que ya pasaron y siguen en estado PENDIENTE
        List<AppointmentEntity> overdueAppointments = appointmentRepository.findOverduePendingAppointments(now);
        
        if (overdueAppointments.isEmpty()) {
            log.info("No se encontraron citas PENDIENTES pasadas para actualizar.");
            return;
        }

        int updatedCount = 0;
        
        // 2. Actualizar el estado de cada cita
        for (AppointmentEntity appointment : overdueAppointments) {
            appointment.setStatus(AppointmentStatus.PASADA);
            // No es estrictamente necesario llamar a save() dentro del bucle
            // si @Transactional está presente, pero es más explícito
            // y garantiza la actualización inmediata si es necesario.
            // Para una gran cantidad de registros, se podría usar saveAll().
            appointmentRepository.save(appointment);
            updatedCount++;
        }
        
        log.info("Tarea finalizada. Total de citas actualizadas de PENDIENTE a PASADA: {}", updatedCount);
    }

     /**
     * Tarea programada para eliminar permanentemente citas que tienen más de 3 meses
     * y están en estado PASADA.
     * Se ejecuta el primer día de cada mes a las 00:30 (media hora después de la otra tarea).
     */
    @Scheduled(cron = "0 30 0 1 * *") // Se ejecuta a las 12:30 AM el día 1 de cada mes
    @Transactional
    public void deleteOldPassedAppointments() {
        log.info("Iniciando tarea programada para eliminar citas PASADA antiguas.");

        // Define la fecha límite: Hoy menos 3 meses.
        // Cualquier cita anterior a esta fecha será eliminada.
        LocalDateTime cutoffDate = LocalDateTime.now().minus(3, ChronoUnit.MONTHS);
        
        // 1. Buscar citas a eliminar
        List<AppointmentEntity> appointmentsToDelete = appointmentRepository.findAppointmentsToDelete(cutoffDate);
        
        if (appointmentsToDelete.isEmpty()) {
            log.info("No se encontraron citas PASADA para eliminar, anteriores a: {}", cutoffDate.toLocalDate());
            return;
        }

        int deletedCount = appointmentsToDelete.size();
        
        // 2. Eliminar las citas
        appointmentRepository.deleteAll(appointmentsToDelete);
        
        log.info("Tarea finalizada. Total de citas eliminadas (PASADA y >3 meses): {}", deletedCount);
    }
}