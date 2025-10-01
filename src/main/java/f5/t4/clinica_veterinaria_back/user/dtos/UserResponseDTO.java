package f5.t4.clinica_veterinaria_back.user.dtos;

import java.util.Set;

public record UserResponseDTO(
    Long id_user,
    String email,
    String dni,
    String name,
    String firstSurname,
    String secondSurname,
    String phoneNumber,
    Set<String> roles
) {
}
