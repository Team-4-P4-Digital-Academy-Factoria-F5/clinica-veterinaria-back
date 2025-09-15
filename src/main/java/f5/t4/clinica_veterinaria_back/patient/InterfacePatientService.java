package f5.t4.clinica_veterinaria_back.patient;

import java.util.List;

import f5.t4.clinica_veterinaria_back.implementations.IService;
import f5.t4.clinica_veterinaria_back.patient.dtos.PatientRequestDTO;
import f5.t4.clinica_veterinaria_back.patient.dtos.PatientResponseDTO;

public interface InterfacePatientService extends IService<PatientResponseDTO, Long>{

    List<PatientResponseDTO> getPatientEntities();
    PatientResponseDTO createEntity(PatientRequestDTO patientRequestDTO);
    PatientResponseDTO getById(Long id);
    PatientResponseDTO updateEntity(Long id, PatientRequestDTO patientRequestDTO);
    void deletePatientEntity(Long id);
}
