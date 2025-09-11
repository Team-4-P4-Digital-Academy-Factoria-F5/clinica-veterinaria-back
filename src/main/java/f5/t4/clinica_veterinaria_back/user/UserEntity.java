package f5.t4.clinica_veterinaria_back.user;
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

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false, unique = true)
    private RoleEntity role;  
    private String name;
    private String last_name;
    private String phone;
    private String email;
}
