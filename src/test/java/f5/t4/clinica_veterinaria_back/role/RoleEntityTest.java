package f5.t4.clinica_veterinaria_back.role;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class RoleEntityTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        RoleEntity role = new RoleEntity();
        role.setId_role(1L);
        role.setName("ROLE_ADMIN");

        assertThat(role.getId_role(), is(1L));
        assertThat(role.getName(), is(equalTo("ROLE_ADMIN")));
    }

    @Test
    void testAllArgsConstructor() {
        RoleEntity role = new RoleEntity(2L, "ROLE_CLIENT", Collections.emptySet());

        assertThat(role.getId_role(), is(2L));
        assertThat(role.getName(), equalTo("ROLE_CLIENT"));
        assertThat(role.getUsers(), is(empty()));
    }
}