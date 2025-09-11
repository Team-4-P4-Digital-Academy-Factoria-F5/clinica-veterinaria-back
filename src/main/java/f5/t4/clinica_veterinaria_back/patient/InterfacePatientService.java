package f5.t4.clinica_veterinaria_back.patient;

import java.util.List;

import f5.t4.clinica_veterinaria_back.implementations.IService;
import f5.t4.clinica_veterinaria_back.patient.dtos.PatientResponseDTO;

public interface InterfacePatientService extends IService<PatientResponseDTO, String>{

    List<PatientResponseDTO> getPatientEntities();
    List<PatientResponseDTO> createEntities();
    List<PatientResponseDTO> getById();
    List<PatientResponseDTO> updateEntity();
    List<PatientResponseDTO> deleteEntity();
}
