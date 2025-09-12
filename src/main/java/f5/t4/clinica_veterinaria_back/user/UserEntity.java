package f5.t4.clinica_veterinaria_back.user;
import java.util.Set;

import f5.t4.clinica_veterinaria_back.profile.ProfileEntity;
import f5.t4.clinica_veterinaria_back.role.RoleEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "roles_users", joinColumns = @JoinColumn(name = "dni"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles;

    @Column(nullable = false, unique = true, length = 100)
    private String email; 
    @Column(nullable = false)
    private String password;

 
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ProfileEntity profile;

}