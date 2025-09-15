package f5.t4.clinica_veterinaria_back.patient;

import java.util.ArrayList;
import java.util.List;

import f5.t4.clinica_veterinaria_back.patient.dtos.PatientRequestDTO;
import f5.t4.clinica_veterinaria_back.patient.dtos.PatientResponseDTO;
import f5.t4.clinica_veterinaria_back.patient.exceptions.PatientNotFoundException;

public class PatientServiceImpl implements InterfacePatientService {
    
    private final PatientRepository repository;

    public PatientServiceImpl(PatientRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PatientResponseDTO> getEntities() {
        List<PatientResponseDTO> patients = new ArrayList<>();

        repository.findAll().forEach(c -> {
            PatientResponseDTO patient = PatientMapper.toDTO(c);
            patients.add(patient);
        });

        return patients;
    }

    @Override
    public PatientResponseDTO createEntity(PatientRequestDTO patientRequestDTO) {
        PatientEntity patient = PatientMapper.toEntity(patientRequestDTO);
        PatientEntity patientStored = repository.save(patient);
        return PatientMapper.toDTO(patientStored) ;
    }

    @Override
    public PatientResponseDTO getById(Long id) {
        PatientEntity patient = repository.findById(id).orElseThrow(() -> new PatientNotFoundException("Paciente no encontrado con id: " + id));
        return PatientMapper.toDTO(patient);
    }

    @Override
    public PatientResponseDTO updateEntity(Long id, PatientRequestDTO patientRequestDTO) {
        PatientEntity patient = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Paciente no encontrado con id: " + id));

        // Actualizar los campos necesarios
        patient.setName(patientRequestDTO.name());
        patient.setImage(patientRequestDTO.image());
        patient.setAge(patientRequestDTO.age());
        patient.setFamily(patientRequestDTO.family());
        patient.setBreed(patientRequestDTO.breed());
        patient.setSex(patientRequestDTO.sex());
        patient.setTutor((patientRequestDTO.tutor()));

        PatientEntity updatedEntity = repository.save(patient);
        return PatientMapper.toDTO(updatedEntity);

    }

    @Override
    public void deletePatientEntity(Long id) {
        PatientEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Paciente no encontrado con id: " + id));
        repository.delete(entity);
    }
    

}
