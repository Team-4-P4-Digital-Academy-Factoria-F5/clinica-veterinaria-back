package f5.t4.clinica_veterinaria_back.appointment.exceptions;

public class AppointmentNotFoundException extends RuntimeException {
    
    public AppointmentNotFoundException(String message) {
        super(message);
    }
    
    public AppointmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}