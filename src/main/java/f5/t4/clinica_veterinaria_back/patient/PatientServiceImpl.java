package f5.t4.clinica_veterinaria_back.patient;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import f5.t4.clinica_veterinaria_back.patient.dtos.PatientRequestDTO;
import f5.t4.clinica_veterinaria_back.patient.dtos.PatientResponseDTO;
import f5.t4.clinica_veterinaria_back.patient.exceptions.PatientException;
import f5.t4.clinica_veterinaria_back.user.UserEntity;
import f5.t4.clinica_veterinaria_back.user.UserRepository;
import f5.t4.clinica_veterinaria_back.user.exceptions.UserNotFoundException;

@Service
public class PatientServiceImpl implements InterfacePatientService {
    
    private final PatientRepository repository;
    private final UserRepository userRepository;

    public PatientServiceImpl(PatientRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
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
        if (patientRequestDTO.tutor() == null) {
            throw new PatientException("User ID cannot be null");
        }
        UserEntity user = userRepository.findById(patientRequestDTO.tutor())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con id: " + patientRequestDTO.tutor()));
        PatientEntity patient = PatientMapper.toEntity(patientRequestDTO);
        patient.setTutor(user);
        PatientEntity patientStored = repository.save(patient);
        return PatientMapper.toDTO(patientStored) ;
    }

    @Override
    public PatientResponseDTO getByID(Long id) {
        PatientEntity patient = repository.findById(id).orElseThrow(() -> new RuntimeException("Paciente no encontrado con id: " + id));
        return PatientMapper.toDTO(patient);
    }

    @Override
    public PatientResponseDTO updateEntity(Long id, PatientRequestDTO patientRequestDTO) {
        if (patientRequestDTO.tutor() == null) {
            throw new PatientException("User ID cannot be null");
        }
        PatientEntity patient = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Paciente no encontrado con id: " + id));

        UserEntity user = userRepository.findById(patientRequestDTO.tutor())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con id: " + patientRequestDTO.tutor()));

        // Actualizar los campos necesarios
        patient.setName(patientRequestDTO.name());
        patient.setImage(patientRequestDTO.image());
        patient.setAge(patientRequestDTO.age());
        patient.setFamily(patientRequestDTO.family());
        patient.setBreed(patientRequestDTO.breed());
        patient.setSex(patientRequestDTO.sex());
        patient.setTutor(user);

        PatientEntity updatedEntity = repository.save(patient);
        return PatientMapper.toDTO(updatedEntity);

    }

    @Override
    public void deleteEntity(Long id) {
        PatientEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Paciente no encontrado con id: " + id));
        repository.delete(entity);
    }
    

}
