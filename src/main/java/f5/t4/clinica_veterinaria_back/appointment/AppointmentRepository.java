package f5.t4.clinica_veterinaria_back.appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import f5.t4.clinica_veterinaria_back.appointment.enums.AppointmentStatus;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {

    @Query("SELECT a FROM AppointmentEntity a WHERE a.patient.id_patient = :patientId")
    List<AppointmentEntity> findByPatient_IdPatient(@Param("patientId") Long patientId);
    
    @Query("SELECT a FROM AppointmentEntity a WHERE a.user.id_user = :userId")
    List<AppointmentEntity> findByUser_IdUser(@Param("userId") Long userId);
    
    List<AppointmentEntity> findByStatus(AppointmentStatus status);
    
    List<AppointmentEntity> findByType(Boolean type);
    
    @Query("SELECT a FROM AppointmentEntity a WHERE a.appointmentDatetime BETWEEN :startDate AND :endDate")
    List<AppointmentEntity> findByAppointmentDatetimeBetween(
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate
    );
    
    @Query("SELECT a FROM AppointmentEntity a WHERE a.patient.id_patient = :patientId AND a.appointmentDatetime >= :fromDate ORDER BY a.appointmentDatetime")
    List<AppointmentEntity> findUpcomingAppointmentsByPatient(
        @Param("patientId") Long patientId, 
        @Param("fromDate") LocalDateTime fromDate
    );
    
    @Query("SELECT a FROM AppointmentEntity a WHERE a.user.id_user = :userId AND a.appointmentDatetime >= :fromDate ORDER BY a.appointmentDatetime")
    List<AppointmentEntity> findUpcomingAppointmentsByUser(
        @Param("userId") Long userId, 
        @Param("fromDate") LocalDateTime fromDate
    );

    @Query("SELECT COUNT(a) FROM AppointmentEntity a WHERE a.appointmentDatetime BETWEEN :startOfDay AND :endOfDay")
    Long countAppointmentsByDay(
        @Param("startOfDay") LocalDateTime startOfDay, 
        @Param("endOfDay") LocalDateTime endOfDay
    );
}