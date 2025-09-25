package f5.t4.clinica_veterinaria_back.patient;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import f5.t4.clinica_veterinaria_back.user.UserEntity;

public class PatientEntityTest {
    @Test
    void testNoArgsConstructorAndSetters() {
        PatientEntity patient = new PatientEntity();

        patient.setId_patient(1L);
        patient.setIdentificationNumber("ABC123");
        patient.setName("Firulais");
        patient.setImage("image.png");
        patient.setAge(5);
        patient.setFamily("Canidae");
        patient.setBreed("Labrador");
        patient.setSex("Male");

        UserEntity tutor = UserEntity.builder()
                .id_user(10L)
                .email("carlos@email.com")
                .password("12345")
                .roles(new HashSet<>())
                .patients(new HashSet<>())
                .build();

        patient.setTutor(tutor);

        assertThat(patient.getId_patient()).isEqualTo(1L);
        assertThat(patient.getIdentificationNumber()).isEqualTo("ABC123");
        assertThat(patient.getName()).isEqualTo("Firulais");
        assertThat(patient.getImage()).isEqualTo("image.png");
        assertThat(patient.getAge()).isEqualTo(5);
        assertThat(patient.getFamily()).isEqualTo("Canidae");
        assertThat(patient.getBreed()).isEqualTo("Labrador");
        assertThat(patient.getSex()).isEqualTo("Male");
        assertThat(patient.getTutor()).isNotNull();
        assertThat(patient.getTutor().getId_user()).isEqualTo(10L);
    }

    @Test
    void testAllArgsConstructor() {
        UserEntity tutor = UserEntity.builder()
                .id_user(20L)
                .email("max.owner@email.com")
                .password("12345")
                .roles(new HashSet<>())
                .patients(new HashSet<>())
                .build();

        PatientEntity patient = new PatientEntity(
                1L,
                "XYZ789",
                "Max",
                "max.png",
                3,
                "Canidae",
                "Golden Retriever",
                "Male",
                tutor
        );

        assertThat(patient.getId_patient()).isEqualTo(1L);
        assertThat(patient.getName()).isEqualTo("Max");
        assertThat(patient.getTutor().getEmail()).isEqualTo("max.owner@email.com");
    }

}
