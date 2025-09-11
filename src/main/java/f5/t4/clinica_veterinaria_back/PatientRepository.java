package f5.t4.clinica_veterinaria_back;

import org.springframework.data.jpa.repository.JpaRepository;

import f5.t4.clinica_veterinaria_back.patient.PatientEntity;

public interface PatientRepository extends JpaRepository<PatientEntity, String>{

}
