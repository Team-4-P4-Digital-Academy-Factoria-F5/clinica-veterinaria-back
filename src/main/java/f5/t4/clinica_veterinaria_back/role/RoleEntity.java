package f5.t4.clinica_veterinaria_back.role;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;
import f5.t4.clinica_veterinaria_back.user.UserEntity;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_role;

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users;

}
