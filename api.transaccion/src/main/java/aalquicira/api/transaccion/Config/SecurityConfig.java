package aalquicira.api.transaccion.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Desactiva CSRF para APIs REST
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api2/auth/**").permitAll() // Permite el login desde el Gateway
                .requestMatchers("/h2-console/**").permitAll() // Opcional: Permite acceso a la consola H2
                .anyRequest().permitAll() // O usa .authenticated() si manejas tokens entre APIs
            )
            .headers(headers -> headers.frameOptions(frame -> frame.disable())); // Para la consola H2

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
