
package f5.t4.clinica_veterinaria_back.user;

import f5.t4.clinica_veterinaria_back.profile.ProfileEntity;
import f5.t4.clinica_veterinaria_back.role.RoleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class UserEntityTest {

    private RoleEntity roleUser;
    private RoleEntity roleAdmin;
    private ProfileEntity profileMargarita;
    private ProfileEntity profileCarlos;

    @BeforeEach
    void setUp() {
        roleUser = new RoleEntity(1L, "ROLE_USER", null);
        roleAdmin = new RoleEntity(2L, "ROLE_ADMIN", null);

        profileMargarita = new ProfileEntity();
        profileMargarita.setName("Margarita");
        profileMargarita.setFirstSurname("López");
        profileMargarita.setSecondSurname("Perez");

        profileCarlos = new ProfileEntity();
        profileCarlos.setName("Carlos");
        profileCarlos.setFirstSurname("Gómez");
        profileCarlos.setSecondSurname("Rodriguez");
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        UserEntity user = new UserEntity();
        user.setId_user(1L);
        user.setEmail("margarita@example.com");
        user.setPassword("securePassword");
        user.setProfile(profileMargarita);
        user.setRoles(new HashSet<>(Set.of(roleAdmin)));

        assertThat(user.getId_user(), is(1L));
        assertThat(user.getEmail(), is("margarita@example.com"));
        assertThat(user.getPassword(), is("securePassword"));

        assertThat(user.getProfile(), allOf(
                hasProperty("name", is("Margarita")),
                hasProperty("firstSurname", is("López")),
                hasProperty("secondSurname", is("Perez"))
        ));

        assertThat(user.getRoles(), hasSize(1));
        assertThat(user.getRoles(), contains(roleAdmin));
    }

 @Test
void testAllArgsConstructor() {
    UserEntity user = new UserEntity(
            2L,
            new HashSet<>(Set.of(roleUser)),
            "carlos@example.com",
            "12345",
            profileCarlos,
            new HashSet<>()
    );

    assertThat(user.getId_user(), is(2L));
    assertThat(user.getEmail(), equalTo("carlos@example.com"));
    assertThat(user.getPassword(), equalTo("12345"));

    assertThat(user.getProfile(), allOf(
            hasProperty("name", is("Carlos")),
            hasProperty("firstSurname", is("Gómez")),
            hasProperty("secondSurname", is("Rodriguez"))
    ));

    assertThat(user.getRoles(), contains(roleUser));
}
    @Test
    void testBuilder() {
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(roleUser);

        UserEntity user = UserEntity.builder()
                .id_user(3L)
                .email("builder@example.com")
                .password("builderPass")
                .profile(profileCarlos)
                .roles(roles)
                .build();

        assertThat(user.getId_user(), is(3L));
        assertThat(user.getEmail(), is("builder@example.com"));
        assertThat(user.getPassword(), is("builderPass"));
        assertThat(user.getProfile(), is(profileCarlos));
        assertThat(user.getRoles(), hasItem(roleUser));
    }
}