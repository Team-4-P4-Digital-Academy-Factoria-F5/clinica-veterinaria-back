package f5.t4.clinica_veterinaria_back.treatment;

import f5.t4.clinica_veterinaria_back.treatment.dtos.TreatmentRequestDTO;

public class TreatmentMapper {
    
    public static TreatmentEntity toEntity(TreatmentRequestDTO dtoRequest) {
        TreatmentEntity treatment = new TreatmentEntity();

        treatment.setName(dtoRequest.name());
        treatment.setDescription(dtoRequest.description());
        treatment.setTreatmentDate(dtoRequest.treatmentDate());

        return treatment;
    }
}