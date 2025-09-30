package f5.t4.clinica_veterinaria_back.treatment;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import f5.t4.clinica_veterinaria_back.patient.PatientEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="treatments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_treatment;

    private String name;
    private String description;

    private LocalDateTime treatmentDate;

    // Relación N:1 con PatientEntity
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_patient", nullable = false)
    @JsonBackReference // no sé si es necesario
    private PatientEntity patient;
}
