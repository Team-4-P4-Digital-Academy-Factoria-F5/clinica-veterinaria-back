package f5.t4.clinica_veterinaria_back.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import f5.t4.clinica_veterinaria_back.user.UserRepository;
import f5.t4.clinica_veterinaria_back.user.exceptions.UserNotFoundException;

public class JpaUserDetailsService implements UserDetailsService {

        private UserRepository userRepository;

    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

        @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {


        return userRepository.findByEmail(email)
                .map(SecurityUser::new)
                .orElseThrow(() -> new UserNotFoundException("User not found with this email"));

    }
}
