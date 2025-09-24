package f5.t4.clinica_veterinaria_back.patient;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import f5.t4.clinica_veterinaria_back.globals.GlobalExceptionHandler;
import f5.t4.clinica_veterinaria_back.implementations.IService;
import f5.t4.clinica_veterinaria_back.patient.dtos.PatientRequestDTO;
import f5.t4.clinica_veterinaria_back.patient.dtos.PatientResponseDTO;

@ExtendWith(MockitoExtension.class)
public class PatientControllerTest {

        private MockMvc mockMvc;

    @Mock
    private IService<PatientResponseDTO, PatientRequestDTO> service;

    @Mock
    private InterfacePatientService patientService;

    @InjectMocks
    private PatientController patientController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(patientController)
                .setControllerAdvice(new GlobalExceptionHandler()) // Si tienes un manejador de excepciones
                .build();
    }

    @Test
    void testIndex() throws Exception {
        PatientResponseDTO dto = new PatientResponseDTO(1L, "12345", "Fido", "img.png", 5, "Canidae", "Labrador", "Male", 10L);
        when(service.getEntities()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Fido"));
    }

    @Test
    void testCreateEntity_Success() throws Exception {
        PatientRequestDTO request = new PatientRequestDTO("12345", "Fido", "img.png", 5, "Canidae", "Labrador", "Male", 10L);
        PatientResponseDTO response = new PatientResponseDTO(1L, "12345", "Fido", "img.png", 5, "Canidae", "Labrador", "Male", 10L);

        when(service.createEntity(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id_patient").value(1L));
    }

    @Test
    void testCreateEntity_BlankName() throws Exception {
        PatientRequestDTO request = new PatientRequestDTO("12345", "", "img.png", 5, "Canidae", "Labrador", "Male", 10L);

        mockMvc.perform(post("/api/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testShow() throws Exception {
        PatientResponseDTO dto = new PatientResponseDTO(1L, "12345", "Fido", "img.png", 5, "Canidae", "Labrador", "Male", 10L);
        when(service.getByID(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fido"));
    }

    @Test
    void testUpdate() throws Exception {
        PatientRequestDTO request = new PatientRequestDTO("12345", "Fido", "img.png", 5, "Canidae", "Labrador", "Male", 10L);
        PatientResponseDTO updated = new PatientResponseDTO(1L, "12345", "Firulais", "img.png", 6, "Canidae", "Labrador", "Male", 10L);

        when(service.updateEntity(eq(1L), any())).thenReturn(updated);

        mockMvc.perform(put("/api/v1/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Firulais"));
    }

    @Test
    void testDelete() throws Exception {
        doNothing().when(service).deleteEntity(1L);

        mockMvc.perform(delete("/api/v1/patients/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetPatientByIdentification() throws Exception {
        PatientResponseDTO dto = new PatientResponseDTO(1L, "ABC123", "Michi", "cat.png", 2, "Felidae", "Siamese", "Female", 20L);
        when(patientService.getByIdentificationNumber("ABC123")).thenReturn(dto);

        mockMvc.perform(get("/api/v1/patients/identification/ABC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Michi"));
    }

    @Test
    void testSearchPatientsByName() throws Exception {
        PatientResponseDTO dto1 = new PatientResponseDTO(1L, "111", "Michi", "cat.png", 2, "Felidae", "Siamese", "Female", 20L);
        PatientResponseDTO dto2 = new PatientResponseDTO(2L, "222", "Michu", "cat2.png", 3, "Felidae", "Persa", "Female", 21L);

        when(patientService.searchByName("Mich")).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/api/v1/patients/search").param("name", "Mich"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Michi"))
                .andExpect(jsonPath("$[1].name").value("Michu"));
    }

}
