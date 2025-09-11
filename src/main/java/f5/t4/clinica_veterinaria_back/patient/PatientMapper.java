package f5.t4.clinica_veterinaria_back.patient;

import f5.t4.clinica_veterinaria_back.patient.dtos.PatientRequestDTO;
import f5.t4.clinica_veterinaria_back.patient.dtos.PatientResponseDTO;

public class PatientMapper {
    
    public static PatientEntity toEntity(PatientRequestDTO dtoRequest) {
        //Añadir entidad User/client? 
        PatientEntity patient = new PatientEntity();
        patient.setName(dtoRequest.name());
        patient.setImage(dtoRequest.image());
        patient.setAge(dtoRequest.age());
        patient.setFamily(dtoRequest.family());
        patient.setBreed(dtoRequest.breed());
        patient.setSex(dtoRequest.sex());
        patient.setDni_client(dtoRequest.dni_client());

        return patient;
    }

    public static PatientResponseDTO toDTO(PatientEntity entity) {
        PatientResponseDTO dtoResponse = new PatientResponseDTO(
            entity.getId_patient(),
            entity.getName(),
            entity.getImage(),
            entity.getAge(),
            entity.getFamily(),
            entity.getBreed(),
            entity.getSex(),
            entity.getDni_client()
        );

        return dtoResponse;
    }

}
