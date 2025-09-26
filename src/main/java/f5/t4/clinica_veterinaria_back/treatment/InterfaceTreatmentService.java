package f5.t4.clinica_veterinaria_back.treatment;

import java.util.List;


import f5.t4.clinica_veterinaria_back.implementations.ITreatmentService;
import f5.t4.clinica_veterinaria_back.treatment.dtos.TreatmentResponseDTO;
import f5.t4.clinica_veterinaria_back.treatment.dtos.TreatmentRequestDTO;




public interface InterfaceTreatmentService extends ITreatmentService {
    
        
       List<TreatmentResponseDTO> getEntities();
       TreatmentEntity createEntity(TreatmentRequestDTO dto);
    //    List<TreatmentEntity> getTreatmentsByPatient(Long patientId);
      
       

             
}
