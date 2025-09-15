package f5.t4.clinica_veterinaria_back.patient.exceptions;

public class PatientNotFoundException extends PatientException{
    
    public PatientNotFoundException(String message) {
        super(message);
    }

    public PatientNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
