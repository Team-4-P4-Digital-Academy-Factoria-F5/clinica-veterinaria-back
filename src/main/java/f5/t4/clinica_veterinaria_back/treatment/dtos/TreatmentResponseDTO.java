package f5.t4.clinica_veterinaria_back.treatment.dtos;
import java.time.LocalDateTime;

public record  TreatmentResponseDTO (
Long Id, // ID generado por DB   
String name,
String description,
LocalDateTime treatmentDate,
Long patientId)// indica a qué paciente pertenece y así se sabe el historial de tramientos
{}
