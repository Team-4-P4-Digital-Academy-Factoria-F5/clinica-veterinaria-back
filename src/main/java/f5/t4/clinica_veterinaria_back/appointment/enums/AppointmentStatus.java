package f5.t4.clinica_veterinaria_back.appointment.enums;

public enum AppointmentStatus {
    PENDIENTE("Pendiente"),
    ATENDIDA("Atendida"),
    PASADA("Pasada");

    private final String displayName;

    AppointmentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static AppointmentStatus fromString(String status) {
        if (status == null) {
            return null;
        }
        
        for (AppointmentStatus appointmentStatus : AppointmentStatus.values()) {
            if (appointmentStatus.name().equalsIgnoreCase(status) || 
                appointmentStatus.displayName.equalsIgnoreCase(status)) {
                return appointmentStatus;
            }
        }
        throw new IllegalArgumentException("Estado de cita no válido: " + status);
    }

    // Método para validar si un string es un estado válido
    public static boolean isValidStatus(String status) {
        if (status == null) {
            return false;
        }
        
        try {
            fromString(status);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}