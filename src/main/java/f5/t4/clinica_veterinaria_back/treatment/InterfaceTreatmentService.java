package f5.t4.clinica_veterinaria_back.treatment;

import java.util.List;

import f5.t4.clinica_veterinaria_back.implementations.ITreatmentService;
import f5.t4.clinica_veterinaria_back.treatment.dtos.TreatmentRequestDTO;
import f5.t4.clinica_veterinaria_back.treatment.dtos.TreatmentResponseDTO;

public interface InterfaceTreatmentService extends ITreatmentService<TreatmentResponseDTO, TreatmentRequestDTO>{


    TreatmentResponseDTO createEntity(Long patientId, TreatmentRequestDTO dto);

    List<TreatmentResponseDTO> getEntityByPatient(Long patientId);

    TreatmentResponseDTO getEntityById(Long treatmentId);

  
}
