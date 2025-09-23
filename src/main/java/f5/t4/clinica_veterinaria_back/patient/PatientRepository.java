package f5.t4.clinica_veterinaria_back.patient;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<PatientEntity, Long>{
    List<PatientEntity> findByTutorIdUser(Long userId);
    List<PatientEntity> findByName(String name);
    Optional<PatientEntity> findByIdentificationNumber(String identificationNumber);

}
