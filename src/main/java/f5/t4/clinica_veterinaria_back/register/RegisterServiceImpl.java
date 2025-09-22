package f5.t4.clinica_veterinaria_back.register;

import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import f5.t4.clinica_veterinaria_back.implementations.IRegisterService;
import f5.t4.clinica_veterinaria_back.profile.ProfileEntity;
import f5.t4.clinica_veterinaria_back.role.RoleEntity;
import f5.t4.clinica_veterinaria_back.role.RoleRepository;
import f5.t4.clinica_veterinaria_back.user.UserEntity;
import f5.t4.clinica_veterinaria_back.role.exceptions.RoleNotFoundException;
import f5.t4.clinica_veterinaria_back.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements IRegisterService<RegisterRequestDTO, RegisterResponseDTO> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

   @Override
public RegisterResponseDTO register(RegisterRequestDTO dto) {

    byte[] decodedPasswordBytes = Base64.getDecoder().decode(dto.password());
    String decodedPassword = new String(decodedPasswordBytes);

    String hashedPassword = passwordEncoder.encode(decodedPassword);

   
    byte[] decodedEmailBytes = Base64.getDecoder().decode(dto.email());
    String decodedEmail = new String(decodedEmailBytes);


    RoleEntity defaultRole = roleRepository.findByName("ROLE_USER")
            .orElseThrow(() -> new RoleNotFoundException("Role not found with name: ROLE_USER"));

    UserEntity user = UserEntity.builder()
            .email(decodedEmail)   
            .password(hashedPassword)
            .roles(Set.of(defaultRole))
            .build();


    ProfileEntity profile = ProfileEntity.builder()
            .dni(dto.dni())
            .name(dto.name())
            .firstSurname(dto.firstSurname())
            .secondSurname(dto.secondSurname())
            .phoneNumber(dto.phoneNumber())
            .user(user)
            .build();

    user.setProfile(profile);

    UserEntity saved = userRepository.save(user);

    return new RegisterResponseDTO(
            saved.getId_user(),
            saved.getEmail(),
            saved.getProfile().getDni(),
            saved.getProfile().getName(),
            saved.getProfile().getFirstSurname(),
            saved.getProfile().getSecondSurname(),
            saved.getProfile().getPhoneNumber(),
            saved.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toSet())
    );
}
}