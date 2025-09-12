package f5.t4.clinica_veterinaria_back.patient;

import java.util.ArrayList;
import java.util.List;

import f5.t4.clinica_veterinaria_back.patient.dtos.PatientRequestDTO;
import f5.t4.clinica_veterinaria_back.patient.dtos.PatientResponseDTO;

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
    public PatientResponseDTO getById(String id) {
        PatientEntity patient = repository.findById(id).orElseThrow(() -> new PatientNotFoundExceptions("Paciente no encontrado. Id " + id + " no existe."));
        return PatientMapper.toDTO(patient);
    }

    @Override
    public PatientResponseDTO updateEntity(String id, PatientRequestDTO patientRequestDTO) {
        PatientEntity patient = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Paciente no encontrado con id: " + id));

        // Actualizar los campos necesarios
        entity.setName(patientRequestDTO.name());
        entity.setImage(patientRequestDTO.image());
        entity.setAge(patientRequestDTO.age());
        entity.setFamily(patientRequestDTO.family());
        entity.setBreed(patientRequestDTO.breed());
        entity.setSex(patientRequestDTO.sex());
        entity.setDni_client(patientRequestDTO.user_id());

        PatientEntity updatedEntity = repository.save(entity);
        return PatientMapper.toDTO(updatedEntity);

    }


    

}
