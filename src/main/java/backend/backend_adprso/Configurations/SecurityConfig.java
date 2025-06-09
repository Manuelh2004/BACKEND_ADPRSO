package backend.backend_adprso.Configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import backend.backend_adprso.Filter.JwtRequestFilter;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public EndsWithUserRequestMatcher endsWithUserRequestMatcher() {
        return new EndsWithUserRequestMatcher(); // Crear una instancia de EndsWithUserRequestMatcher manualmente
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(request -> endsWithUserRequestMatcher().matches((HttpServletRequest) request)) // Usar EndsWithUserRequestMatcher para rutas que terminan con "/user"
                .permitAll()  // Permitir acceso a esas rutas

                // También permitir las rutas que comienzan con "/auth/"
                .requestMatchers("/auth/**").permitAll()

                .anyRequest().authenticated()  // Requiere autenticación para cualquier otra ruta
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
            )
            .sessionManagement(sess -> sess
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Usamos Stateless porque estamos trabajando con JWT
            );

        // Añadimos el filtro JWT antes del filtro de autenticación por usuario y contraseña
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}