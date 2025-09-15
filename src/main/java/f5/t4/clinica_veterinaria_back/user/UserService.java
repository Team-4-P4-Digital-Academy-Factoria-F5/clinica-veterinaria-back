package f5.t4.clinica_veterinaria_back.user;

import java.util.Optional;

import f5.t4.clinica_veterinaria_back.implementations.IService;
import f5.t4.clinica_veterinaria_back.user.dtos.*;

public interface UserService extends IService<UserResponseDTO, UserRequestDTO> {
    Optional<UserResponseDTO> findByEmail(String email);
}