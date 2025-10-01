package f5.t4.clinica_veterinaria_back.patient;

import java.util.List;

import f5.t4.clinica_veterinaria_back.implementations.IService;
import f5.t4.clinica_veterinaria_back.patient.dtos.PatientRequestDTO;
import f5.t4.clinica_veterinaria_back.patient.dtos.PatientResponseDTO;

public interface InterfacePatientService extends IService<PatientResponseDTO, PatientRequestDTO>{

    List<PatientResponseDTO> getEntities();
    PatientResponseDTO createEntity(PatientRequestDTO patientRequestDTO);
    PatientResponseDTO getByID(Long id);
    PatientResponseDTO updateEntity(Long id, PatientRequestDTO patientRequestDTO);
    void deleteEntity(Long id);

    PatientResponseDTO getByIdentificationNumber(String identificationNumber);
    List<PatientResponseDTO> searchByName(String name);
}

