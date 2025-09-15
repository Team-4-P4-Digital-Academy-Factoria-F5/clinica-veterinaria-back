package f5.t4.clinica_veterinaria_back.globals;

import java.time.LocalDateTime;

public record GlobalExceptionResponseDTO(
       int status,
        String error,
        String message,
        LocalDateTime timestamp
) {}
