package f5.t4.clinica_veterinaria_back.patient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import f5.t4.clinica_veterinaria_back.implementations.IService;
import f5.t4.clinica_veterinaria_back.patient.dtos.PatientRequestDTO;
import f5.t4.clinica_veterinaria_back.patient.dtos.PatientResponseDTO;
import f5.t4.clinica_veterinaria_back.user.UserEntity;

@WebMvcTest(controllers = PatientController.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IService<PatientResponseDTO, PatientRequestDTO> service;

    @Autowired
    private ObjectMapper objectMapper;

    private UserEntity buildTutor() {
        return UserEntity.builder()
                .id_user(10L)
                .email("carlos@email.com")
                .password("12345")
                .roles(new HashSet<>())
                .patients(new HashSet<>())
                .build();
    }

    private PatientRequestDTO buildRequest(String name, int age) {
        return new PatientRequestDTO(
                "ID-123",
                name,
                name.toLowerCase() + ".png",
                age,
                "Perro",
                "Labrador",
                "Male",
                buildTutor()
        );
    }

    private PatientResponseDTO buildResponse(Long id, String name, int age) {
        return new PatientResponseDTO(
                id,
                "ID-" + id,
                name,
                name.toLowerCase() + ".png",
                age,
                "Perro",
                "Labrador",
                "Male",
                buildTutor()
        );
    }

    @Test
    void testIndex() throws Exception {
        Mockito.when(service.getEntities())
                .thenReturn(List.of(buildResponse(1L, "Firulais", 5)));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Firulais"))
                .andExpect(jsonPath("$[0].tutor.email").value("carlos@email.com"));
    }

    @Test
    void testCreateEntity_Valid() throws Exception {
        PatientRequestDTO request = buildRequest("Max", 3);
        PatientResponseDTO response = buildResponse(1L, "Max", 3);

        Mockito.when(service.createEntity(any(PatientRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Max"))
                .andExpect(jsonPath("$.tutor.id_user").value(10L));
    }

    @Test
    void testCreateEntity_InvalidName() throws Exception {
        PatientRequestDTO request = buildRequest(" ", 3);

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testShow() throws Exception {
        Mockito.when(service.getByID(1L))
                .thenReturn(buildResponse(1L, "Firulais", 5));

        mockMvc.perform(get("/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Firulais"))
                .andExpect(jsonPath("$.tutor.email").value("carlos@email.com"));
    }

    @Test
    void testUpdate() throws Exception {
        PatientRequestDTO request = buildRequest("Max", 4);
        PatientResponseDTO response = buildResponse(1L, "Max", 4);

        Mockito.when(service.updateEntity(eq(1L), any(PatientRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(put("/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.age").value(4))
                .andExpect(jsonPath("$.tutor.id_user").value(10L));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/1"))
                .andExpect(status().isNoContent());
    }

}
