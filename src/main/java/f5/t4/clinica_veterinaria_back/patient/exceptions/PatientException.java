package f5.t4.clinica_veterinaria_back.patient.exceptions;

public class PatientException extends RuntimeException{
    public PatientException(String message) {
        super(message);
    }

    public PatientException(String message, Throwable cause) {
        super(message, cause);
    }
}
