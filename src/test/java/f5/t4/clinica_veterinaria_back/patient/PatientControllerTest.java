package f5.t4.clinica_veterinaria_back.patient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import f5.t4.clinica_veterinaria_back.implementations.IService;
import f5.t4.clinica_veterinaria_back.patient.dtos.PatientRequestDTO;
import f5.t4.clinica_veterinaria_back.patient.dtos.PatientResponseDTO;

@WebMvcTest(controllers = PatientController.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IService<PatientResponseDTO, PatientRequestDTO> patientService;
    
    @Autowired
    ObjectMapper mapper;

    @Test
    @DisplayName("Should return all patients")
    void testIndex_ShouldReturnAPatients() throws Exception {
        PatientResponseDTO milka = new PatientResponseDTO();
        PatientResponseDTO oreo = new PatientResponseDTO();

        List<PatientResponseDTO> patients = List.of(milka, oreo);
        String json = mapper.writeValueAsString(patients);

        when(patientService.getEntities()).thenReturn(patients);
        MockHttpServletResponse response = mockMvc.perform(get("/api/v1/patients"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.getContentAsString(), is(equalTo(json)));
    }

    

    @Test
    void testStore_ShouldReturnStatus201() throws Exception {
        PatientRequestDTO dto = new PatientRequestDTO("NV34T7", "Milka", "", 2, "gato", "bengalí", "hembra", 12312312B);
        PatientResponseDTO milka = new PatientResponseDTO(1L, "NV34T7", "Milka", "", 2, "gato", "bengalí", "hembra", 12312312B);
        String json = mapper.writeValueAsString(dto);


        when(patientService.createEntity(dto)).thenReturn(milka);
        MockHttpServletResponse response = mockMvc
                .perform(post("/api/v1/patients").content(json).contentType("application/json"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        assertThat(response.getContentAsString(), containsString(milka.name()));
    }

    @Test
    void testSavePatient_ShouldReturnStatus400_IfNameIsEmpty() throws Exception {
        PatientRequestDTO dto = new PatientRequestDTO("");
        String json = mapper.writeValueAsString(dto);
        when(patientService.createEntity(dto)).thenReturn(null);
        mockMvc.perform(post("/api/v1/patients").content(json).contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreatePatient_ShouldReturnNoContent_IfServiceDoesNotReturnAnyValue() throws Exception {
        PatientRequestDTO dto = new PatientRequestDTO("A23M87", "Milka", "", 2, "gato", "bengalí", "hembra", 12312312B);
        String json = mapper.writeValueAsString(dto);

        when(patientService.createEntity(dto)).thenReturn(null);
        mockMvc.perform(post("/api/v1/patients").content(json).contentType("application/json"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAPatientById_ShouldReturnStatus200_And_Patient() throws Exception {
        Long pathVariable = 1L;
        PatienResponseDTO oreo = new PatientResponseDTO(2L, "A23R87",  "Oreo", "", 3, "gato", "siamés", "hembra", 12121212C);

        when(patientService.getByID(pathVariable)).thenReturn(oreo);
        MockHttpServletResponse response = mockMvc.perform(get("/api/v1/patients/{id}", pathVariable))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        assertThat(response.getContentAsString(), containsString(oreo.name()));
    }

    @Test
    @DisplayName("Should update patient and return 200")
    void testUpdatePatient_ShouldReturnStatus200() throws Exception {
        Long pathVariable = 1L;
        PatientRequestDTO dto = new PatientRequestDTO("AB23C54", "Milka", "", 3, "gato", "bengalí", "hembra", 12312312L);
        PatientResponseDTO updated = new PatientResponseDTO(1L, "AB23C54", "Milka", "", 3, "gato", "bengalí", "hembra", 12312312L);
        String json = mapper.writeValueAsString(dto);

        when(patientService.updateEntity(pathVariable, dto)).thenReturn(updated);

        mockMvc.perform(put("/patients/{id}", pathVariable)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Milka Updated")));
    }

    @Test
    @DisplayName("Should delete patient and return 204")
    void testDeletePatient_ShouldReturnStatus204() throws Exception {
        Long pathVariable = 1L;

        doNothing().when(patientService).deleteEntity(pathVariable);

        mockMvc.perform(delete("/patients/{id}", pathVariable))
            .andExpect(status().isNoContent());
    }

}
