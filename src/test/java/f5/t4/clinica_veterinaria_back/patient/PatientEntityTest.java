package f5.t4.clinica_veterinaria_back.patient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

public class PatientEntityTest {
    @Test
    void testPatientEntity_Initialization() {
        PatientEntity patient = new PatientEntity(1L, "A34B12", "Milka", "a", 2, "gato", "siamés", "hembra", 1234567A);

        assertThat(patient, is(instanceOf(PatientEntity.class)));
        assertThat(patient.getClass().getDeclaredFields().length, is(equalTo(8)));
    }

}
