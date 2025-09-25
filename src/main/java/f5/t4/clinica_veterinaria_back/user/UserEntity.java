package f5.t4.clinica_veterinaria_back.user;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import f5.t4.clinica_veterinaria_back.profile.ProfileEntity;
import f5.t4.clinica_veterinaria_back.role.RoleEntity;
import jakarta.persistence.*;
import lombok.*;
import f5.t4.clinica_veterinaria_back.patient.PatientEntity;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_user;

    //Relación con Roles
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "roles_users", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles;

    @Column(nullable = false, unique = true, length = 100)
    private String email; 
    @Column(nullable = false)
    private String password;

    //Relación con Profile
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ProfileEntity profile;

    //Relación con Pacientes, que nos permite eliminarlos en cascada cuando borramos el user correspondiente,    
    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<PatientEntity> patients = new HashSet<>();
}



