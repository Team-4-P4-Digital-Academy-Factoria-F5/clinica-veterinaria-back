package f5.t4.clinica_veterinaria_back.role.exceptions;

public class RoleException extends RuntimeException {
    
    public RoleException(String message) {
        super(message);
    }

    public RoleException(String message, Throwable cause) {
        super(message, cause);
    }

}
