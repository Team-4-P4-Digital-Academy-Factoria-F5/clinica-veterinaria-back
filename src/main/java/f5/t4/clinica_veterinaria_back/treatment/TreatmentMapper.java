package f5.t4.clinica_veterinaria_back.treatment;

import f5.t4.clinica_veterinaria_back.treatment.dtos.TreatmentRequestDTO;
import f5.t4.clinica_veterinaria_back.treatment.dtos.TreatmentResponseDTO;

public class TreatmentMapper {
    
    public static TreatmentEntity toEntity(TreatmentRequestDTO dtoRequest) {
        TreatmentEntity treatment = new TreatmentEntity();

        treatment.setName(dtoRequest.name());
        treatment.setDescription(dtoRequest.description());
        treatment.setTreatmentDate(dtoRequest.treatmentDate());

        return treatment;
    }

    // public static TreatmentResponseDTO toDTO(TreatmentEntity entity) {
    //     return new TreatmentResponseDTO(
    //         entity.getId_treatment(),
    //         entity.getName(),
    //         entity.getDescription(),
    //         entity.getTreatmentDate(),
    //         entity.getPatient().getId_patient()
    //     );
    // }
}