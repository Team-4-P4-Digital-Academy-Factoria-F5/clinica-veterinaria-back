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
        user.setUsername("margarita");
        user.setPassword("securePassword");

        ProfileEntity profile = new ProfileEntity();
        profile.setFullName("Margarita López");
        user.setProfile(profile);

        RoleEntity role = new RoleEntity(1L, "ROLE_ADMIN", null);
        user.setRoles(new HashSet<>(Set.of(role)));

        assertThat(user.getId_user(), is(1L));
        assertThat(user.getUsername(), is("margarita"));
        assertThat(user.getPassword(), is("securePassword"));

        assertThat(user.getProfile().getFullName(), is("Margarita López"));
        assertThat(user.getRoles(), hasSize(1));
        assertThat(user.getRoles(), contains(role));
    }

    @Test
    void testAllArgsConstructor() {
        RoleEntity role = new RoleEntity(2L, "ROLE_CLIENT", null);
        ProfileEntity profile = new ProfileEntity();
        profile.setFullName("Carlos Gómez");

        UserEntity user = new UserEntity(
                2L,
                new HashSet<>(Set.of(role)),
                "carlos",
                "12345",
                profile
        );

        assertThat(user.getId_user(), is(2L));
        assertThat(user.getUsername(), equalTo("carlos"));
        assertThat(user.getPassword(), equalTo("12345"));
        assertThat(user.getProfile().getFullName(), equalTo("Carlos Gómez"));
        assertThat(user.getRoles(), contains(role));
    }
}