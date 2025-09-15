package f5.t4.clinica_veterinaria_back.user;

import f5.t4.clinica_veterinaria_back.role.RoleEntity;
import f5.t4.clinica_veterinaria_back.profile.ProfileEntity;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class UserEntityTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        UserEntity user = new UserEntity();
        user.setId_user(1L);
        user.setEmail("margarita@example.com");
        user.setPassword("securePassword");

        ProfileEntity profile = new ProfileEntity();
        profile.setName("Margarita");
        profile.setFirstSurname("López");
        profile.setSecondSurname("Perez");
        user.setProfile(profile);

        RoleEntity role = new RoleEntity(2L, "ROLE_ADMIN", null);
        user.setRoles(new HashSet<>(Set.of(role)));

        assertThat(user.getId_user(), is(1L));
        assertThat(user.getEmail(), is("margarita@example.com"));
        assertThat(user.getPassword(), is("securePassword"));

        assertThat(user.getProfile().getName(), is("Margarita"));
        assertThat(user.getProfile().getFirstSurname(), is("López"));
        assertThat(user.getProfile().getSecondSurname(), is("Perez"));
        assertThat(user.getRoles(), hasSize(1));
        assertThat(user.getRoles(), contains(role));
    }

    @Test
    void testAllArgsConstructor() {
        RoleEntity role = new RoleEntity(1L, "ROLE_USER", null);
        ProfileEntity profile = new ProfileEntity();
        profile.setName("Carlos");
        profile.setFirstSurname("Gómez");
        profile.setSecondSurname("Rodriguez");

        UserEntity user = new UserEntity(
                1L,
                new HashSet<>(Set.of(role)),
                "carlos@example.com",
                "12345",
                profile
        );

        assertThat(user.getId_user(), is(2L));
        assertThat(user.getEmail(), equalTo("carlos@example.com"));
        assertThat(user.getPassword(), equalTo("12345"));
        assertThat(user.getProfile().getName(), equalTo("Carlos"));
        assertThat(user.getProfile().getFirstSurname(), equalTo("Gómez"));
        assertThat(user.getProfile().getSecondSurname(), equalTo("Rodriguez"));
        assertThat(user.getRoles(), contains(role));
    }
}
