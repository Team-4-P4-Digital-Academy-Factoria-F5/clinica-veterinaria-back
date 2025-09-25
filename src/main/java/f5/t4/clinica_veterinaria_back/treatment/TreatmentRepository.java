package f5.t4.clinica_veterinaria_back.treatment;

  import java.util.List;
  import java.util.Optional;
  import org.springframework.data.jpa.repository.JpaRepository;
  import f5.t4.clinica_veterinaria_back.patient.PatientEntity;

    public class TreatmentRepository {
    
        public interface PatientRepository extends JpaRepository<PatientEntity, Long>{
            List<PatientEntity> findByName(String name);
            Optional<PatientEntity> findByIdentificationNumber(String identificationNumber);
    
    }
}
