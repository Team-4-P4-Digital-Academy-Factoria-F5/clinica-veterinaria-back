package f5.t4.clinica_veterinaria_back.appointment;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AppointmentRepositoryTest {

    @Test
    void repositoryInterface_ShouldExtendJpaRepository() {
        assertTrue(org.springframework.data.jpa.repository.JpaRepository.class
                .isAssignableFrom(AppointmentRepository.class));
    }

    @Test
    void repositoryInterface_ShouldHaveFindByPatientMethod() throws NoSuchMethodException {
        assertNotNull(AppointmentRepository.class
                .getMethod("findByPatient_IdPatient", Long.class));
    }

    @Test
    void repositoryInterface_ShouldHaveFindByUserMethod() throws NoSuchMethodException {
        assertNotNull(AppointmentRepository.class
                .getMethod("findByUser_IdUser", Long.class));
    }

    @Test
    void repositoryInterface_ShouldHaveFindByStatusMethod() throws NoSuchMethodException {
        assertNotNull(AppointmentRepository.class
                .getMethod("findByStatus", f5.t4.clinica_veterinaria_back.appointment.enums.AppointmentStatus.class));
    }

    @Test
    void repositoryInterface_ShouldHaveFindByTypeMethod() throws NoSuchMethodException {
        assertNotNull(AppointmentRepository.class
                .getMethod("findByType", Boolean.class));
    }

    @Test
    void repositoryInterface_ShouldHaveFindByDatetimeBetweenMethod() throws NoSuchMethodException {
        assertNotNull(AppointmentRepository.class
                .getMethod("findByAppointmentDatetimeBetween", 
                    java.time.LocalDateTime.class, 
                    java.time.LocalDateTime.class));
    }

    @Test
    void repositoryInterface_ShouldHaveFindUpcomingByPatientMethod() throws NoSuchMethodException {
        assertNotNull(AppointmentRepository.class
                .getMethod("findUpcomingAppointmentsByPatient", 
                    Long.class, 
                    java.time.LocalDateTime.class));
    }

    @Test
    void repositoryInterface_ShouldHaveFindUpcomingByUserMethod() throws NoSuchMethodException {
        assertNotNull(AppointmentRepository.class
                .getMethod("findUpcomingAppointmentsByUser", 
                    Long.class, 
                    java.time.LocalDateTime.class));
    }

    @Test
    void repositoryInterface_ShouldHaveCountByDayMethod() throws NoSuchMethodException {
        assertNotNull(AppointmentRepository.class
                .getMethod("countAppointmentsByDay", 
                    java.time.LocalDateTime.class, 
                    java.time.LocalDateTime.class));
    }

    @Test
    void repositoryInterface_ShouldHaveFindOverduePendingMethod() throws NoSuchMethodException {
        assertNotNull(AppointmentRepository.class
                .getMethod("findOverduePendingAppointments", 
                    java.time.LocalDateTime.class));
    }

    @Test
    void repositoryInterface_ShouldHaveFindAppointmentsToDeleteMethod() throws NoSuchMethodException {
        assertNotNull(AppointmentRepository.class
                .getMethod("findAppointmentsToDelete", 
                    java.time.LocalDateTime.class));
    }

    @Test
    void repositoryInterface_ShouldBeAnInterface() {
        assertTrue(AppointmentRepository.class.isInterface());
    }

    @Test
    void repositoryInterface_ShouldBeInCorrectPackage() {
        assertEquals("f5.t4.clinica_veterinaria_back.appointment", 
                AppointmentRepository.class.getPackageName());
    }
}