package f5.t4.clinica_veterinaria_back.profile;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import f5.t4.clinica_veterinaria_back.user.UserEntity;

public class ProfileEntityTest {
    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = UserEntity.builder()
        .id_user(1L)
        .email("user@example.com")
        .password("securePass")
        .build();
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        ProfileEntity profile = new ProfileEntity();
        profile.setId_profile(1L);
        profile.setDni("12345678A");
        profile.setName("Margarita");
        profile.setFirstSurname("López");
        profile.setSecondSurname("Pérez");
        profile.setPhoneNumber("600123456");
        profile.setUser(user);

        assertThat(profile.getId_profile(), is(1L));
        assertThat(profile.getDni(), is("12345678A"));
        assertThat(profile.getName(), is("Margarita"));
        assertThat(profile.getFirstSurname(), is("López"));
        assertThat(profile.getSecondSurname(), is("Pérez"));
        assertThat(profile.getPhoneNumber(), is("600123456"));
        assertThat(profile.getUser(), is(user));
    }

    @Test
    void testAllArgsConstructor() {
        ProfileEntity profile = new ProfileEntity(
            2L,
            "87654321",
            "Carlos",
            "Gómez",
            "Rodriguez",
            "699123456",
            user);

        assertThat(profile.getId_profile(), is(2L));
        assertThat(profile.getDni(), is("87654321"));
        assertThat(profile.getName(), is("Carlos"));
        assertThat(profile.getFirstSurname(), is("Gómez"));
        assertThat(profile.getSecondSurname(), is("Rodriguez"));
        assertThat(profile.getPhoneNumber(), is("699123456"));
        assertThat(profile.getUser(), is(user));

    }

    @Test
    void testBuilder() {
        ProfileEntity profile = ProfileEntity.builder()
        .id_profile(3L)
        .dni("111112222C")
        .name("Laura")
        .firstSurname("Martínez")
        .secondSurname("Santos")
        .phoneNumber("688888888")
        .user(user)
        .build();

        assertThat(profile.getId_profile(), is(3L));
        assertThat(profile.getDni(), is("111112222C"));
        assertThat(profile.getName(), is("Laura"));
        assertThat(profile.getFirstSurname(), is("Martínez"));
        assertThat(profile.getSecondSurname(), is("Santos"));
        assertThat(profile.getPhoneNumber(), is("688888888"));
        assertThat(profile.getUser(), is(user));
    }
}
