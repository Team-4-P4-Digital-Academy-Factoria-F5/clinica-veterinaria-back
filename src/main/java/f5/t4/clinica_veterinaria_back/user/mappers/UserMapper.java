package f5.t4.clinica_veterinaria_back.user.mappers;

import f5.t4.clinica_veterinaria_back.profile.ProfileEntity;
import f5.t4.clinica_veterinaria_back.role.RoleEntity;
import f5.t4.clinica_veterinaria_back.role.RoleRepository;
import f5.t4.clinica_veterinaria_back.user.UserEntity;
import f5.t4.clinica_veterinaria_back.user.dtos.UserRequestDTO;
import f5.t4.clinica_veterinaria_back.user.dtos.UserResponseDTO;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final RoleRepository roleRepository;

    public UserMapper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public UserEntity userRequestDtoToUserEntity(UserRequestDTO userRequestDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userRequestDTO.email());
        userEntity.setPassword(userRequestDTO.password());

        ProfileEntity profile = new ProfileEntity();
        profile.setDni(userRequestDTO.dni());
        profile.setName(userRequestDTO.name());
        profile.setFirstSurname(userRequestDTO.firstSurname());
        profile.setSecondSurname(userRequestDTO.secondSurname());
        profile.setPhoneNumber(userRequestDTO.phoneNumber());
        profile.setUser(userEntity);
        userEntity.setProfile(profile);

        Set<RoleEntity> roles = userRequestDTO.roles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());
        userEntity.setRoles(roles);

        return userEntity;
    }

    public UserResponseDTO userEntityToUserResponseDto(UserEntity userEntity) {
        Set<String> roles = userEntity.getRoles().stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toSet());

        return new UserResponseDTO(
                userEntity.getId_user(),
                userEntity.getEmail(),
                userEntity.getProfile().getDni(),
                userEntity.getProfile().getName(),
                userEntity.getProfile().getFirstSurname(),
                userEntity.getProfile().getSecondSurname(),
                userEntity.getProfile().getPhoneNumber(),
                roles
        );
    }
}
