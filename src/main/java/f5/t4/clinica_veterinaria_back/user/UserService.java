package f5.t4.clinica_veterinaria_back.user;

import java.util.Optional;


import f5.t4.clinica_veterinaria_back.implementations.IUserService;
import f5.t4.clinica_veterinaria_back.user.dtos.*;

public interface UserService extends IUserService<UserResponseDTO, UserRequestDTO> {
    Optional<UserResponseDTO> findByEmail(String email);
}