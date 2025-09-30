package f5.t4.clinica_veterinaria_back.treatment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import f5.t4.clinica_veterinaria_back.patient.PatientEntity;

public class TreatmentEntityTest {

     @Test
    void testTreatmentEntitySettersAndGetters() {
        // Arrange
        PatientEntity patient = new PatientEntity();
        patient.setId_patient(1L);
        patient.setName("Firulais");

        LocalDateTime treatmentDate = LocalDateTime.of(2025, 9, 30, 10, 30);

        TreatmentEntity treatment = new TreatmentEntity();

        // Act
        treatment.setId_treatment(100L);
        treatment.setName("Vacuna antirrábica");
        treatment.setDescription("Aplicación de vacuna anual contra la rabia");
        treatment.setTreatmentDate(treatmentDate);
        treatment.setPatient(patient);

        // Assert
        assertThat(treatment.getId_treatment()).isEqualTo(100L);
        assertThat(treatment.getName()).isEqualTo("Vacuna antirrábica");
        assertThat(treatment.getDescription()).isEqualTo("Aplicación de vacuna anual contra la rabia");
        assertThat(treatment.getTreatmentDate()).isEqualTo(treatmentDate);
        assertThat(treatment.getPatient()).isNotNull();
        assertThat(treatment.getPatient().getName()).isEqualTo("Firulais");
    }

      @Test
    void testAllArgsConstructor() {
        PatientEntity patient = new PatientEntity();
            patient.setId_patient(1L);
            patient.setName("Firulais");
            patient.setFamily("Perro");
            patient.setBreed("Labrador");
            patient.setAge(3);
            patient.setTutor(null); //

        LocalDateTime treatmentDate = LocalDateTime.of(2025, 10, 1, 15, 0);

        TreatmentEntity treatment = new TreatmentEntity(
                200L,
                "Desparasitación",
                "Tratamiento mensual para parásitos internos",
                treatmentDate,
                patient);

        assertThat(treatment.getId_treatment()).isEqualTo(200L);
        assertThat(treatment.getName()).isEqualTo("Desparasitación");
        assertThat(treatment.getPatient().getId_patient()).isEqualTo(1L);
    }

        //Treatment empty y assertNull
     @Test
    void testNoArgsConstructor() {
        TreatmentEntity treatment = new TreatmentEntity();

        assertNotNull(treatment);
        assertNull(treatment.getId_treatment());
        assertNull(treatment.getName());
        assertNull(treatment.getDescription());
        assertNull(treatment.getTreatmentDate());
        assertNull(treatment.getPatient());
    }

        @Test
    void testSetPatient() {
        PatientEntity patient = new PatientEntity();
        patient.setId_patient(1L);
        patient.setName("Firulais");

        TreatmentEntity treatment = new TreatmentEntity();
        treatment.setPatient(patient);

        assertNotNull(treatment.getPatient());
        assertEquals("Firulais", treatment.getPatient().getName());
    }


}
