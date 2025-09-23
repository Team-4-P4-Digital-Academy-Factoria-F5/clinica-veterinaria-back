package f5.t4.clinica_veterinaria_back.user;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import f5.t4.clinica_veterinaria_back.patient.PatientEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Set<PatientEntity> findByPatients();
}