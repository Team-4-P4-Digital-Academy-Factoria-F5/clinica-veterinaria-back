package f5.t4.clinica_veterinaria_back.user.dtos;

import java.util.Set;

public record UserRequestDTO(
    String email,
    String password,
    String dni,
    String name,
    String firstSurname,
    String secondSurname,
    String phoneNumber,
    Set<String> roles
) {
}
