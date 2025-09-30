package f5.t4.clinica_veterinaria_back.patient;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import f5.t4.clinica_veterinaria_back.appointment.AppointmentEntity;
import f5.t4.clinica_veterinaria_back.user.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name="patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor 
public class PatientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_patient;

    private String identificationNumber;
    private String name;
    private String image;
    private int age;
    private String family;
    private String breed;
    private String sex;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="user_id", nullable=false)
    @JsonBackReference
    private UserEntity tutor;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference("patient-appointments")
    private Set<AppointmentEntity> appointments = new HashSet<>();
    
}
