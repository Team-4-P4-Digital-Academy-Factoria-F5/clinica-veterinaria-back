package f5.t4.clinica_veterinaria_back.patient;

import f5.t4.clinica_veterinaria_back.patient.dtos.PatientRequestDTO;
import f5.t4.clinica_veterinaria_back.patient.dtos.PatientResponseDTO;

public class PatientMapper {
    
    public static PatientEntity toEntity(PatientRequestDTO dtoRequest) {
        PatientEntity patient = new PatientEntity();
        patient.setIdentificationNumber(dtoRequest.identificationNumber());
        patient.setName(dtoRequest.name());
        patient.setImage(dtoRequest.image());
        patient.setAge(dtoRequest.age());
        patient.setFamily(dtoRequest.family());
        patient.setBreed(dtoRequest.breed());
        patient.setSex(dtoRequest.sex());
        return patient;
    }

    public static PatientResponseDTO toDTO(PatientEntity entity) {
        PatientResponseDTO dtoResponse = new PatientResponseDTO(
            entity.getId_patient(),
            entity.getIdentificationNumber(),
            entity.getName(),
            entity.getImage(),
            entity.getAge(),
            entity.getFamily(),
            entity.getBreed(),
            entity.getSex(),
            entity.getTutor() != null ? entity.getTutor().getId_user() : null
        );

        return dtoResponse;
    }

}