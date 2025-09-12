package f5.t4.clinica_veterinaria_back.profile;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import f5.t4.clinica_veterinaria_back.user.UserEntity;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_profile;

    @Column(nullable = false, unique = true, length = 20)
    private String dni;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 150)
    private String firstSurname;

    @Column(nullable = false, length = 150)
    private String secondSurname;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;
}