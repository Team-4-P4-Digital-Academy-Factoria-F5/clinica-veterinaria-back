package f5.t4.clinica_veterinaria_back.patient;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import f5.t4.clinica_veterinaria_back.patient.dtos.PatientRequestDTO;
import f5.t4.clinica_veterinaria_back.patient.dtos.PatientResponseDTO;
import f5.t4.clinica_veterinaria_back.patient.exceptions.PatientException;
import f5.t4.clinica_veterinaria_back.user.UserEntity;
import f5.t4.clinica_veterinaria_back.user.UserRepository;
import f5.t4.clinica_veterinaria_back.user.exceptions.UserNotFoundException;

public class PatientServiceImplTest {
    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PatientServiceImpl patientService;

    private UserEntity tutor;
    private PatientEntity patient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

         tutor = new UserEntity();
        tutor.setId_user(1L);
        tutor.setEmail("tutor@example.com");
        tutor.setPassword("password");

        patient = new PatientEntity();
        patient.setId_patient(1L);
        patient.setIdentificationNumber("ID123");
        patient.setName("Firulais");
        patient.setImage("dog.png");
        patient.setAge(5);
        patient.setFamily("Canine");
        patient.setBreed("Labrador");
        patient.setSex("Male");
        patient.setTutor(tutor);
    }

   @Test
    void testGetEntities_returnsList() {
        when(patientRepository.findAll()).thenReturn(List.of(patient));

        List<PatientResponseDTO> result = patientService.getEntities();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Firulais");
    }

    @Test
    void testCreateEntity_success() {
        PatientRequestDTO request = new PatientRequestDTO(
                "ID123", "Firulais", "dog.png", 5,
                "Canine", "Labrador", "Male", tutor.getId_user()
        );

        when(userRepository.findById(tutor.getId_user())).thenReturn(Optional.of(tutor));
        when(patientRepository.save(any(PatientEntity.class))).thenReturn(patient);

        PatientResponseDTO response = patientService.createEntity(request);

        assertThat(response.name()).isEqualTo("Firulais");
        verify(patientRepository).save(any(PatientEntity.class));
    }

    @Test
    void testCreateEntity_userIdNull_throwsException() {
        PatientRequestDTO request = new PatientRequestDTO(
                "ID123", "Firulais", "dog.png", 5,
                "Canine", "Labrador", "Male", null
        );

        assertThrows(PatientException.class, () -> patientService.createEntity(request));
    }

    @Test
    void testCreateEntity_userNotFound_throwsException() {
        PatientRequestDTO request = new PatientRequestDTO(
                "ID123", "Firulais", "dog.png", 5,
                "Canine", "Labrador", "Male", 999L
        );

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> patientService.createEntity(request));
    }

    @Test
    void testGetByID_success() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        PatientResponseDTO response = patientService.getByID(1L);

        assertThat(response.name()).isEqualTo("Firulais");
    }

    @Test
    void testGetByID_notFound_throwsException() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> patientService.getByID(1L));
    }

    @Test
    void testUpdateEntity_success() {
        PatientRequestDTO request = new PatientRequestDTO(
                "ID124", "Max", "dog2.png", 3,
                "Canine", "Beagle", "Male", tutor.getId_user()
        );

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(userRepository.findById(tutor.getId_user())).thenReturn(Optional.of(tutor));
        when(patientRepository.save(any(PatientEntity.class))).thenReturn(patient);

        PatientResponseDTO response = patientService.updateEntity(1L, request);

        assertThat(response.name()).isEqualTo("Max");
        verify(patientRepository).save(any(PatientEntity.class));
    }

    @Test
    void testUpdateEntity_userIdNull_throwsException() {
        PatientRequestDTO request = new PatientRequestDTO(
                "ID124", "Max", "dog2.png", 3,
                "Canine", "Beagle", "Male", null
        );

        assertThrows(PatientException.class, () -> patientService.updateEntity(1L, request));
    }

    @Test
    void testUpdateEntity_notFound_throwsException() {
        PatientRequestDTO request = new PatientRequestDTO(
                "ID124", "Max", "dog2.png", 3,
                "Canine", "Beagle", "Male", tutor.getId_user()
        );

        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> patientService.updateEntity(1L, request));
    }

    @Test
    void testDeleteEntity_success() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        patientService.deleteEntity(1L);

        verify(patientRepository).delete(patient);
    }

    @Test
    void testDeleteEntity_notFound_throwsException() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> patientService.deleteEntity(1L));
    }

    @Test
    void testGetByIdentificationNumber_success() {
        when(patientRepository.findByIdentificationNumber("ID123"))
                .thenReturn(Optional.of(patient));

        PatientResponseDTO response = patientService.getByIdentificationNumber("ID123");

        assertThat(response.name()).isEqualTo("Firulais");
    }

    @Test
    void testGetByIdentificationNumber_notFound_throwsException() {
        when(patientRepository.findByIdentificationNumber("X999"))
                .thenReturn(Optional.empty());

        assertThrows(PatientException.class, () -> patientService.getByIdentificationNumber("X999"));
    }

    @Test
    void testSearchByName_success() {
        when(patientRepository.findByName("Firulais")).thenReturn(List.of(patient));

        List<PatientResponseDTO> result = patientService.searchByName("Firulais");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Firulais");
    }
}
