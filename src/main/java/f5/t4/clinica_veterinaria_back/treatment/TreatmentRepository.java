package f5.t4.clinica_veterinaria_back.treatment;

  import java.util.List;
  import org.springframework.data.jpa.repository.JpaRepository;
 
    public interface TreatmentRepository extends JpaRepository<TreatmentEntity, Long> {
    // Método para traer los tratamientos de un paciente concreto
    List<TreatmentEntity> findByPatient_IdPatient(Long patientId);
}

    
    

