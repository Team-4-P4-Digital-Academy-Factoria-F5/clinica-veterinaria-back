// package f5.t4.clinica_veterinaria_back.user;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import f5.t4.clinica_veterinaria_back.patient.PatientEntity;
// import f5.t4.clinica_veterinaria_back.role.RoleEntity;
// import f5.t4.clinica_veterinaria_back.user.dtos.UserRequestDTO;
// import f5.t4.clinica_veterinaria_back.user.dtos.UserResponseDTO;
// import org.junit.jupiter.api.Test;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.web.servlet.MockMvc;

// import java.util.List;
// import java.util.Optional;
// import java.util.Set;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// @SpringBootTest
// @AutoConfigureMockMvc
// class UserControllerIntegrationTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @Autowired
//     private ObjectMapper objectMapper;

//     @MockBean
//     private UserRepository userRepository;

//     @MockBean
//     private UserService userService;

//     private final String BASE_URL = "/api/v17users";

//     //GET /users (solo ADMIN)
//     @Test
//     @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
//     void testGetAllUsers_AsAdmin() throws Exception {
//         UserResponseDTO user = new UserResponseDTO(
//             1L, "test@example.com", "12345678", "Test", "User", "Second", "987654321", Set.of("ROLE_USER"));
//     }
    
//     //GET /users/ {id} (admin)
//     @Test
//     @WithMockUser(username = "user@example.com", roles = {"USER"})
//     void testGetUserById_AsOwner() throws Exception {
//         RoleEntity role = new RoleEntity(1L, "ROLE_USER", null);
//         UserEntity userEntity = UserEntity.builder()
//         .id_user(1L)
//         .email("user@example.com")
//         .roles(Set.of(role))
//         .build();

//         UserResponseDTO responseDTO = new UserResponseDTO(
//             1L, "user@example.com", "12345678", "Name", "Surname", "SecondSurname", "987654321", Set.of("ROLE_USER")
//         );

//         when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(userEntity));
//         when(userService.getByID(1L)).thenReturn(responseDTO);

//         mockMvc.perform(get(BASE_URL + "/1"))
//         .andExpect(status().isOk())
//         .andExpect(jsonPath("$.email").value("test@example.com"));
//     }

//     //PUT /users/ {id}
//     @Test
//     @WithMockUser(username = "user@example.com", roles = {"USER"})
//     void testUpdateUser_AsOwner() throws Exception {
//         RoleEntity role = new RoleEntity(1L, "ROLE_USER", null);
//         UserEntity userEntity = UserEntity.builder()
//         .id_user(1L)
//         .email("user@example.com")
//         .roles(Set.of(role))
//         .build();

//         UserRequestDTO requestDTO = new UserRequestDTO(
//             "update@example.com", "newPass", "12345678", "Updated", "User", "Second", "987654321", Set.of("ROLE_USER")
//         );

//         UserResponseDTO responseDTO = new UserResponseDTO(
//             1L, "update@example.com", "12345678", "Updated", "User", "Second", "987654321", Set.of("ROLE_USER")
//         );

//         when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(userEntity));
//         when(userService.updateEntity(1L, requestDTO)).thenReturn(responseDTO);

//         mockMvc.perform(put(BASE_URL + "/1")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(requestDTO)))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.email").value("updated@example.com"));
//     }

//     //DELETE /users/ {id}
//     @Test
//     @WithMockUser(username = "user@example.com", roles = {"USER"})
//     void testDeleteUser_AsOwner() throws Exception {
//         RoleEntity role = new RoleEntity(1L, "ROLE_USER", null);
//         UserEntity userEntity = UserEntity.builder()
//                 .id_user(1L)
//                 .email("user@example.com")
//                 .roles(Set.of(role))
//                 .build();

//         when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(userEntity));

//         mockMvc.perform(delete(BASE_URL + "/1"))
//                 .andExpect(status().isNoContent());
//     } 

//     //GET /users / me
//     @Test
//     @WithMockUser(username = "user@example.com", roles = {"USER"})
//     void testGetCurrentUser() throws Exception {
//         RoleEntity role = new RoleEntity(1L, "ROLE_USER", null);
//         UserEntity userEntity = UserEntity.builder()
//                 .id_user(1L)
//                 .email("user@example.com")
//                 .roles(Set.of(role))
//                 .build();

//         UserResponseDTO responseDTO = new UserResponseDTO(
//                 1L, "user@example.com", "12345678", "Name", "Surname",
//                 "SecondSurname", "987654321", Set.of("ROLE_USER")
//         );

//         when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(userEntity));
//         when(userService.getByID(1L)).thenReturn(responseDTO);

//         mockMvc.perform(get(BASE_URL + "/me"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.email").value("user@example.com"));
//     }
// }