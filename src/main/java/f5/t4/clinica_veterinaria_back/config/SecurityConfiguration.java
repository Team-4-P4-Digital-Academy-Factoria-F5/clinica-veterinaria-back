package f5.t4.clinica_veterinaria_back.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import f5.t4.clinica_veterinaria_back.security.JpaUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Value("${api-endpoint}")
    String endpoint;

    private JpaUserDetailsService jpaUserDetailsService;

    public SecurityConfiguration(JpaUserDetailsService jpaUserDetailsService) {
        this.jpaUserDetailsService = jpaUserDetailsService;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                        .disable())
                .headers(header -> header
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                // Solo se permite que tu aplicación sea embebida en un <iframe> si el origen
                // (dominio) es el mismo.
                .formLogin(form -> form.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.POST, endpoint + "/register").permitAll()
                        .requestMatchers(endpoint + "/patients").permitAll()   
                        .requestMatchers(endpoint + "/login").hasAnyRole("USER", "ADMIN") // principio de mínimos privilegios
                        .anyRequest().authenticated())
                        .userDetailsService(jpaUserDetailsService)
                        .httpBasic(withDefaults())
                          .sessionManagement(session -> session
                                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        // http.headers(header -> header.frameOptions(frame -> frame.sameOrigin()));

        return http.build();

    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    
}

