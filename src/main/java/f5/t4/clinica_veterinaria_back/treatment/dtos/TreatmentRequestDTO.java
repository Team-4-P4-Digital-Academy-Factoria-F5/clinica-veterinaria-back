package f5.t4.clinica_veterinaria_back.treatment.dtos;

import java.time.LocalDateTime;

public record  TreatmentRequestDTO (
    
String name,
String description,
LocalDateTime treatmentDate,
Long patientId ) {

    

}

