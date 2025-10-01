package f5.t4.clinica_veterinaria_back.user;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import f5.t4.clinica_veterinaria_back.profile.ProfileEntity;
import f5.t4.clinica_veterinaria_back.role.RoleEntity;
import f5.t4.clinica_veterinaria_back.role.RoleRepository;
import f5.t4.clinica_veterinaria_back.role.exceptions.RoleNotFoundException;
import f5.t4.clinica_veterinaria_back.user.dtos.UserRequestDTO;
import f5.t4.clinica_veterinaria_back.user.dtos.UserResponseDTO;
import f5.t4.clinica_veterinaria_back.user.exceptions.UserNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock 
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity userEntity;
    private UserResponseDTO userResponseDTO;
    private ProfileEntity profileEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        profileEntity = new ProfileEntity();
        profileEntity.setName("Test");
        profileEntity.setFirstSurname("User");


        userEntity = UserEntity.builder()
        .id_user(1L)
        .email("test@example.com")
        .password("encodePass")
        .profile(profileEntity)
        .build();

        userResponseDTO = new UserResponseDTO(
            1L,
            "test@example.com",
            "12345678",
            "Test",
            "User",
            "SecondSurname",
            "987654321",
            Set.of("ROLE_USER")
        );
    }

    //getEntities
    @Test 
    void testGetEntities() {
        when(userRepository.findAll()).thenReturn(List.of(userEntity));
        when(userMapper.userEntityToUserResponseDto(userEntity)).thenReturn(userResponseDTO);

        List<UserResponseDTO> result = userService.getEntities();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test@example.com", result.get(0).email());
    }

    //getByID positivo
    @Test
    void testGetById_UserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userMapper.userEntityToUserResponseDto(userEntity)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.getByID(1L);

        assertNotNull(result);
        assertEquals("test@example.com", result.email());
    }

    //getByID usuario no encontrado
    @Test
    void testGetById_UserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getByID(99L));
    }

    //updateEntity positivo
    @Test
    void testUpdateEntity_UserExists() {
        UserRequestDTO requestDTO = new UserRequestDTO(
            "newemail@example.com",
            "newPass",
            "12345678",
            "NewName",
            "NewSurname",
            "SecondSurname",
            "123456789",
            Set.of("ROLE_ADMIN")
        );

        RoleEntity roleAdmin = new RoleEntity(1L, "ROLE_ADMIN", null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(roleAdmin));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userMapper.userEntityToUserResponseDto(userEntity)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.updateEntity(1L, requestDTO);

        assertNotNull(result);
        assertEquals("test@example.com", result.email());
        verify(userRepository).save(userEntity);
    }

    //UpdateEntity user no encontrado
    @Test
    void testUpdateEntity_UserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        UserRequestDTO requestDTO = mock(UserRequestDTO.class);

        assertThrows(UserNotFoundException.class, () -> userService.updateEntity(99L, requestDTO));
    }

    //UpdateEntity rol no encontrado
    @Test
    void testUpdateEntity_RoleNotFound() {
        UserRequestDTO requestDTO = new UserRequestDTO(
            "email@test.com",
            "pass",
            "dni",
            "name",
            "surname",
            "secondSurname",
            "phone",
            Set.of("ROLE_UNKNOWN")
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(roleRepository.findByName("ROLE_UNKNOWN")).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> userService.updateEntity(1L, requestDTO));
    }

    //deleteEntity existente
    @Test
    void testDeleteEntity_UserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteEntity(1L);

        verify(userRepository).deleteById(1L);
    }

    //deleteEntity no encontrado
    @Test
    void testDeleteEntity_UserNotFound() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteEntity(99L));
    }

    //findByEmail positivo
    @Test
    void testFindByEmail_UserExists() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userEntity));
        when(userMapper.userEntityToUserResponseDto(userEntity)).thenReturn(userResponseDTO);

        Optional<UserResponseDTO> result = userService.findByEmail("test@example.com");

        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().email());
    }

    //findByEmail no encontrado
    @Test
    void testFindByEmail_UserNotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());
        
        Optional<UserResponseDTO> result = userService.findByEmail("notfound@example.com");

        assertFalse(result.isPresent());
    }

}
