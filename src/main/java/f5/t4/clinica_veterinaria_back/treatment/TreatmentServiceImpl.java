package f5.t4.clinica_veterinaria_back.treatment;

import java.util.List;

import org.springframework.stereotype.Service;

import f5.t4.clinica_veterinaria_back.patient.PatientEntity;
import f5.t4.clinica_veterinaria_back.patient.PatientRepository;
import f5.t4.clinica_veterinaria_back.treatment.dtos.TreatmentRequestDTO;
import f5.t4.clinica_veterinaria_back.treatment.dtos.TreatmentResponseDTO;

@Service
public class TreatmentServiceImpl implements InterfaceTreatmentService {

    private final TreatmentRepository treatmentRepository;
    private final PatientRepository patientRepository;

    public TreatmentServiceImpl(TreatmentRepository treatmentRepository, PatientRepository patientRepository) {
        this.treatmentRepository = treatmentRepository;
        this.patientRepository = patientRepository;
    }

    // -------------------------
    // Métodos de InterfaceTreatmentService (específicos)
    // -------------------------

    // Dentro de TreatmentServiceImpl

    @Override
    public List<TreatmentResponseDTO> getEntities() {
        // Implementación para obtener TODOS los tratamientos (sin filtrar por paciente)
        return treatmentRepository.findAll()
                .stream()
                .map(TreatmentMapper::toDTO)
                .toList();
    }


    @Override
    public TreatmentResponseDTO createEntity(Long patientId, TreatmentRequestDTO dto) {
        PatientEntity patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        TreatmentEntity treatment = TreatmentMapper.toEntity(dto);
        treatment.setPatient(patient);

        TreatmentEntity savedTreatment = treatmentRepository.save(treatment);
        return TreatmentMapper.toDTO(savedTreatment);
    }

    @Override
    public List<TreatmentResponseDTO> getEntityByPatient(Long patientId) {
        return treatmentRepository.findByPatient_IdPatient(patientId)
                .stream()
                .map(TreatmentMapper::toDTO)
                .toList();
    }

   @Override
    public TreatmentResponseDTO getEntityById(Long treatmentId) {
        TreatmentEntity treatment = treatmentRepository.findById(treatmentId)
            .orElseThrow(() -> new RuntimeException("Tratamiento no encontrado"));

    // Seguridad extra: paciente nunca debería ser null, pero por si acaso
    Long patientId = treatment.getPatient() != null ? treatment.getPatient().getId_patient() : null;

    return new TreatmentResponseDTO(
        treatment.getId_treatment(),
        treatment.getName(),
        treatment.getDescription(),
        treatment.getTreatmentDate(),
        patientId
    );
}


   
}
