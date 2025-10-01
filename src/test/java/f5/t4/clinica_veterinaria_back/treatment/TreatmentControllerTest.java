package f5.t4.clinica_veterinaria_back.treatment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import f5.t4.clinica_veterinaria_back.treatment.dtos.TreatmentRequestDTO;
import f5.t4.clinica_veterinaria_back.treatment.dtos.TreatmentResponseDTO;

class TreatmentControllerTest {

    @Mock
    private InterfaceTreatmentService treatmentService;

    @InjectMocks
    private TreatmentController treatmentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

        // treatment ById
    @Test
    void testGetTreatmentById() {
        Long treatmentId = 1L;

        TreatmentResponseDTO mockTreatment = new TreatmentResponseDTO(
                treatmentId,
                "Vacuna",
                "Vacuna anual",
                LocalDateTime.now(),
                5L
        );

        when(treatmentService.getEntityById(treatmentId)).thenReturn(mockTreatment);

        ResponseEntity<TreatmentResponseDTO> response = treatmentController.getTreatmentById(treatmentId);

        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().Id()).isEqualTo(treatmentId);
        assertThat(response.getBody().name()).isEqualTo("Vacuna");
        assertThat(response.getBody().patientId()).isEqualTo(5L);

        verify(treatmentService, times(1)).getEntityById(treatmentId);
    }

     @Test
    void testCreateTreatment() {
        Long patientId = 5L; 

        // Creamos el DTO de request
        TreatmentRequestDTO request = new TreatmentRequestDTO(
                "Vacuna",
                "Vacuna anual",
                LocalDateTime.now(),
                patientId
        );

        // Creamos el DTO de respuesta que devolverá el mock
        TreatmentResponseDTO mockTreatment = new TreatmentResponseDTO(
                1L, // id generado
                "Vacuna",
                "Vacuna anual",
                LocalDateTime.now(),
                patientId
        );

        // Simulamos la creación en el service
        when(treatmentService.createEntity(eq(patientId), any())).thenReturn(mockTreatment);

        ResponseEntity<TreatmentResponseDTO> response = treatmentController.createTreatment(patientId, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().name()).isEqualTo("Vacuna");
        assertThat(response.getBody().patientId()).isEqualTo(patientId);

        verify(treatmentService, times(1)).createEntity(eq(patientId), any());
    }


    @Test
    void testGetTreatmentsByPatient() {
    Long patientId = 5L;

    // Creamos algunos tratamientos de ejemplo
    TreatmentResponseDTO treatment1 = new TreatmentResponseDTO(
            1L, "Vacuna", "Vacuna anual", LocalDateTime.now(), patientId
    );
    TreatmentResponseDTO treatment2 = new TreatmentResponseDTO(
            2L, "Desparasitación", "Desparasitación interna", LocalDateTime.now(), patientId
    );

    List<TreatmentResponseDTO> mockTreatments = List.of(treatment1, treatment2);

    // Simulamos el service
    when(treatmentService.getEntityByPatient(patientId)).thenReturn(mockTreatments);

    // Llamamos al controller
    ResponseEntity<List<TreatmentResponseDTO>> response = treatmentController.getTreatmentsByPatient(patientId);

    // Verificaciones
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().size()).isEqualTo(2);
    assertThat(response.getBody().get(0).name()).isEqualTo("Vacuna");
    assertThat(response.getBody().get(1).name()).isEqualTo("Desparasitación");

    // Verificamos que el service fue llamado una vez
        verify(treatmentService, times(1)).getEntityByPatient(patientId);
    }
    // ID non exist
    @Test
    void testGetTreatmentById_NotFound() {
        Long treatmentId = 99L;

        when(treatmentService.getEntityById(treatmentId)).thenReturn(null);

        ResponseEntity<TreatmentResponseDTO> response = treatmentController.getTreatmentById(treatmentId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK); // o NOT_FOUND si después lo cambiás
        assertThat(response.getBody()).isNull();

        verify(treatmentService, times(1)).getEntityById(treatmentId);
    }

    //Empty List
    @Test
    void testGetTreatmentsByPatient_EmptyList() {
        Long patientId = 5L;
        when(treatmentService.getEntityByPatient(patientId)).thenReturn(List.of());

        ResponseEntity<List<TreatmentResponseDTO>> response = treatmentController.getTreatmentsByPatient(patientId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    // service return null
    @Test
    void testCreateTreatment_NullResponse() {
        Long patientId = 5L;
        TreatmentRequestDTO request = new TreatmentRequestDTO("Vacuna", "Vacuna anual", LocalDateTime.now(), patientId);

        when(treatmentService.createEntity(eq(patientId), any())).thenReturn(null);

        ResponseEntity<TreatmentResponseDTO> response = treatmentController.createTreatment(patientId, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNull();

        verify(treatmentService, times(1)).createEntity(eq(patientId), any());
    }


}