package f5.t4.clinica_veterinaria_back.treatment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import f5.t4.clinica_veterinaria_back.patient.PatientEntity;
import f5.t4.clinica_veterinaria_back.patient.PatientRepository;
import f5.t4.clinica_veterinaria_back.treatment.TreatmentEntity;
import f5.t4.clinica_veterinaria_back.treatment.TreatmentRepository;
import f5.t4.clinica_veterinaria_back.treatment.TreatmentServiceImpl;
import f5.t4.clinica_veterinaria_back.treatment.dtos.TreatmentRequestDTO;
import f5.t4.clinica_veterinaria_back.treatment.dtos.TreatmentResponseDTO;

public class TreatmentServiceImplTest {

    @Mock
    private TreatmentRepository treatmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private TreatmentServiceImpl treatmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
        void testCreateEntity_success() {
        
        Long patientId = 1L;
        TreatmentRequestDTO dto = new TreatmentRequestDTO("Vacuna", "Vacuna anual", null, patientId);

        PatientEntity patient = new PatientEntity();
        patient.setId_patient(patientId);

        TreatmentEntity treatment = new TreatmentEntity();
        treatment.setId_treatment(10L);
        treatment.setName(dto.name());
        treatment.setDescription(dto.description());
        treatment.setPatient(patient);

        // Mockear repositorios
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(treatmentRepository.save(any(TreatmentEntity.class))).thenReturn(treatment);

        // Ejecutar método
        TreatmentResponseDTO result = treatmentService.createEntity(patientId, dto);

       
        assertNotNull(result);
        assertEquals("Vacuna", result.name());
        assertEquals("Vacuna anual", result.description());
        assertEquals(patientId, result.patientId());

        // interacción con los repositorios
        verify(patientRepository, times(1)).findById(patientId);
        verify(treatmentRepository, times(1)).save(any(TreatmentEntity.class));
    }

        @Test
        void testGetEntityByPatient_returnsDTOsForPatient() {
        // Datos de ejemplo
        Long patientId = 1L;

        PatientEntity patient = new PatientEntity();
        patient.setId_patient(patientId);

        TreatmentEntity treatment1 = new TreatmentEntity();
        treatment1.setId_treatment(10L);
        treatment1.setName("Vacuna");
        treatment1.setDescription("Vacuna anual");
        treatment1.setPatient(patient);

        TreatmentEntity treatment2 = new TreatmentEntity();
        treatment2.setId_treatment(11L);
        treatment2.setName("Desparasitación");
        treatment2.setDescription("Desparasitación interna");
        treatment2.setPatient(patient);

        // Mockear repositorio
        when(treatmentRepository.findByPatientId(patientId))
                .thenReturn(List.of(treatment1, treatment2));

        // Ejecutar método
        List<TreatmentResponseDTO> result = treatmentService.getEntityByPatient(patientId);

        // Verificaciones
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Vacuna", result.get(0).name());
        assertEquals("Desparasitación", result.get(1).name());
        assertEquals(patientId, result.get(0).patientId());
        assertEquals(patientId, result.get(1).patientId());

        // Verificar interacción con repositorio
        verify(treatmentRepository, times(1)).findByPatientId(patientId);
    }

    @Test
        void testGetEntityById_success() {
            // Datos de ejemplo
            Long treatmentId = 10L;
            Long patientId = 1L;

            PatientEntity patient = new PatientEntity();
            patient.setId_patient(patientId);

            TreatmentEntity treatment = new TreatmentEntity();
            treatment.setId_treatment(treatmentId);
            treatment.setName("Vacuna");
            treatment.setDescription("Vacuna anual");
            treatment.setPatient(patient);

            // Mockear repositorio
            when(treatmentRepository.findById(treatmentId)).thenReturn(Optional.of(treatment));

            // Ejecutar método
            TreatmentResponseDTO result = treatmentService.getEntityById(treatmentId);

            // Verificaciones
            assertNotNull(result);
            assertEquals(treatmentId, result.Id());
            assertEquals("Vacuna", result.name());
            assertEquals("Vacuna anual", result.description());
            assertEquals(patientId, result.patientId());

            // Verificar interacción
            verify(treatmentRepository, times(1)).findById(treatmentId);
    }
        @Test
            void testGetEntityById_notFound_throwsException() {
            // Datos de ejemplo
            Long treatmentId = 10L;

            // Mockear repositorio para que devuelva Optional vacío
            when(treatmentRepository.findById(treatmentId)).thenReturn(Optional.empty());

            // Ejecutar y verificar que lanza RuntimeException
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                treatmentService.getEntityById(treatmentId);
            });

            // Verificar mensaje de la excepción
            assertEquals("Tratamiento no encontrado", exception.getMessage());

            // Verificar interacción con repositorio
            verify(treatmentRepository, times(1)).findById(treatmentId);

    }



}
