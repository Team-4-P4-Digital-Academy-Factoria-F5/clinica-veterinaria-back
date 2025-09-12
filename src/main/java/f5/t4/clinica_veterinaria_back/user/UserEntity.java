package f5.t4.clinica_veterinaria_back.user;
import java.util.Set;

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
    private Long dni;

    @ManyToMany
    @JoinTable(name = "roles_users", joinColumns = @JoinColumn(name = "dni"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles;
    
    private String name;
    private String last_name;
    private String phone;
    private String email;
}