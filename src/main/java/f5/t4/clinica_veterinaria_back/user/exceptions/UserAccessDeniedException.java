package f5.t4.clinica_veterinaria_back.user.exceptions;

public class UserAccessDeniedException extends UserException {

    public UserAccessDeniedException(String message) {
        super(message);
    }

    public UserAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}