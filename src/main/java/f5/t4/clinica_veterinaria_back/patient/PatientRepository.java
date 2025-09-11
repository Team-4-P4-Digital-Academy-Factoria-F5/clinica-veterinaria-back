package f5.t4.clinica_veterinaria_back.patient;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<PatientEntity, String>{

}
