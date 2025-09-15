package f5.t4.clinica_veterinaria_back.user;

import java.util.List;
import java.util.Set;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.management.relation.Role;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import f5.t4.clinica_veterinaria_back.profile.ProfileEntity;
import f5.t4.clinica_veterinaria_back.role.RoleEntity;
import f5.t4.clinica_veterinaria_back.role.RoleRepository;
import f5.t4.clinica_veterinaria_back.role.exceptions.RoleNotFoundException;
import f5.t4.clinica_veterinaria_back.user.dtos.UserRequestDTO;
import f5.t4.clinica_veterinaria_back.user.dtos.UserResponseDTO;
import f5.t4.clinica_veterinaria_back.user.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponseDTO> getEntities() {
        return userRepository.findAll().stream()
                .map(userMapper::userEntityToUserResponseDto)
                .toList();
    }

    @Override
    public UserResponseDTO createEntity(UserRequestDTO dto) {
        UserEntity entity = userMapper.userRequestDtoToUserEntity(dto);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        return userMapper.userEntityToUserResponseDto(userRepository.save(entity));
    }

    @Override
    public UserResponseDTO getByID(Long id) {
        return userRepository.findById(id)
                .map(userMapper::userEntityToUserResponseDto)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    public UserResponseDTO updateEntity(Long id, UserRequestDTO dto) {
        UserEntity user = userRepository.findById(id)
                  .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));

        ProfileEntity profile = user.getProfile();
        profile.setDni(dto.dni());
        profile.setName(dto.name());
        profile.setFirstSurname(dto.firstSurname());
        profile.setSecondSurname(dto.secondSurname());
        profile.setPhoneNumber(dto.phoneNumber());

        java.util.Set<RoleEntity> roles = dto.roles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RoleNotFoundException(roleName)))
                .collect(Collectors.toSet());
        user.setRoles(roles);

        return userMapper.userEntityToUserResponseDto(userRepository.save(user));
    }

    @Override
    public void deleteEntity(Long id) {
        if (!userRepository.existsById(id)) {
            throw  new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public Optional<UserResponseDTO> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::userEntityToUserResponseDto);
    }
}