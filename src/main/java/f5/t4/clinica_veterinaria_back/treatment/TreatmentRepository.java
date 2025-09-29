package f5.t4.clinica_veterinaria_back.treatment;

  import java.util.List;
  import org.springframework.data.jpa.repository.JpaRepository;
  import org.springframework.data.jpa.repository.Query;
  import org.springframework.data.repository.query.Param;
 
    public interface TreatmentRepository extends JpaRepository<TreatmentEntity, Long> {
    // Método para traer los tratamientos de un paciente concreto
    //  List<TreatmentEntity> findByPatient_Id_patient(Long patientId);
      @Query("SELECT t FROM TreatmentEntity t WHERE t.patient.id_patient = :patientId")
      List<TreatmentEntity> findByPatientId(@Param("patientId") Long patientId);

}

    
    

