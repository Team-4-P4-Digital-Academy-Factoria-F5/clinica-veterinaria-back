package f5.t4.clinica_veterinaria_back.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import f5.t4.clinica_veterinaria_back.role.exceptions.RoleNotFoundException;
import f5.t4.clinica_veterinaria_back.user.exceptions.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<GlobalExceptionResponseDTO> handleRoleNotFound(RoleNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<GlobalExceptionResponseDTO> handleUserNotFound(UserNotFoundException ex) {
    return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalExceptionResponseDTO> handleGeneral(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + ex.getMessage());
    }

    private ResponseEntity<GlobalExceptionResponseDTO> buildResponse(HttpStatus status, String message) {
        GlobalExceptionResponseDTO error = new GlobalExceptionResponseDTO(
                status.value(),
                status.getReasonPhrase(),
                message,
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(error);
    }

    }

