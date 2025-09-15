package f5.t4.clinica_veterinaria_back.register;
import java.util.Set;

public record RegisterRequestDTO(
        String email,
        String password,
        String dni,
        String name,
        String firstSurname,
        String secondSurname,
        String phoneNumber,
        Set<String> roles
) {}