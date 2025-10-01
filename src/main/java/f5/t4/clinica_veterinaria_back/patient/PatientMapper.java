package f5.t4.clinica_veterinaria_back.patient;

import f5.t4.clinica_veterinaria_back.patient.dtos.PatientRequestDTO;
import f5.t4.clinica_veterinaria_back.patient.dtos.PatientResponseDTO;
import f5.t4.clinica_veterinaria_back.profile.ProfileEntity;

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
        Long tutorId = null;
        String tutorFullName = null;

        if (entity.getTutor() != null) {
            tutorId = entity.getTutor().getId_user();

            // Accedemos al perfil para obtener el nombre completo
            if (entity.getTutor().getProfile() != null) {
                ProfileEntity profile = entity.getTutor().getProfile();
                tutorFullName = profile.getName() + " " + profile.getFirstSurname() + " " + profile.getSecondSurname();
            }
        }

        // Retornamos el DTO con todos los campos, incluyendo tutor y su nombre completo
        return new PatientResponseDTO(
            entity.getId_patient(),
            entity.getIdentificationNumber(),
            entity.getName(),
            entity.getImage(),
            entity.getAge(),
            entity.getFamily(),
            entity.getBreed(),
            entity.getSex(),
            tutorId,
            tutorFullName
        );
    }

}