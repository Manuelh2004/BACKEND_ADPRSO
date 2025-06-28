package backend.backend_adprso.Configurations;

import java.util.List;

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
        return new EndsWithUserRequestMatcher(); 
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
         http.cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173"));
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
            .csrf(csrf -> csrf.disable()) 
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(request -> endsWithUserRequestMatcher().matches((HttpServletRequest) request)) // Usar EndsWithUserRequestMatcher para rutas que terminan con "/user"Add commentMore actions
                .permitAll()  // Permitir acceso a esas rutas
                .requestMatchers("/auth/**").permitAll() 
                .requestMatchers("/admin/**").hasAuthority("Administrador")  // Rutas /admin/** solo accesibles para los ADMIN
                .requestMatchers("/user/**").hasAuthority("Usuario")  // Rutas /user/** solo accesibles para los USER
                .anyRequest().authenticated() // Requiere autenticación para cualquier otra ruta
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // Manejo de errores de autenticación
                .accessDeniedHandler(jwtAccessDeniedHandler) // Manejo de errores de acceso denegado
            )
            .sessionManagement(sess -> sess
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) 
            );

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);  

        return http.build();
    }
  
}