package f5.t4.clinica_veterinaria_back.register;

import java.util.Base64;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import f5.t4.clinica_veterinaria_back.profile.ProfileEntity;
import f5.t4.clinica_veterinaria_back.role.RoleEntity;
import f5.t4.clinica_veterinaria_back.role.RoleRepository;
import f5.t4.clinica_veterinaria_back.role.exceptions.RoleNotFoundException;
import f5.t4.clinica_veterinaria_back.user.UserEntity;
import f5.t4.clinica_veterinaria_back.user.UserRepository;

public class RegisterServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegisterServiceImpl registerService;

    private RegisterRequestDTO requestDTO;
    private RoleEntity roleUser;
    private UserEntity savedUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Datos de entrada (Base64)
        String emailBase64 = Base64.getEncoder().encodeToString("user@example.com".getBytes());
        String passwordBase64 = Base64.getEncoder().encodeToString("plainPassword".getBytes());

        requestDTO = new RegisterRequestDTO(
                emailBase64,
                passwordBase64,
                "12345678A",
                "TestName",
                "TestSurname",
                "TestSecondSurname",
                "600123456",
                Set.of("ROLE_USER")
        );

        roleUser = new RoleEntity(1L, "ROLE_USER", null);

        savedUser = UserEntity.builder()
                .id_user(1L)
                .email("user@example.com")
                .password("encodedPassword")
                .roles(Set.of(roleUser))
                .profile(ProfileEntity.builder()
                        .dni("12345678A")
                        .name("TestName")
                        .firstSurname("TestSurname")
                        .secondSurname("TestSecondSurname")
                        .phoneNumber("600123456")
                        .build())
                .build();
    }

    @Test
    void testRegister_Success() {
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(roleUser));
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);

        RegisterResponseDTO response = registerService.register(requestDTO);

        assertNotNull(response);
        assertEquals(1L, response.id_user());
        assertEquals("user@example.com", response.email());
        assertEquals("12345678A", response.dni());
        assertEquals("TestName", response.name());
        assertEquals("TestSurname", response.firstSurname());
        assertTrue(response.roles().contains("ROLE_USER"));

        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void testRegister_RoleNotFound() {
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> registerService.register(requestDTO));

        verify(userRepository, never()).save(any());
    }
}
