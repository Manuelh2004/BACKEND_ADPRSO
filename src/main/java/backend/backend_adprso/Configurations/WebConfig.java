package backend.backend_adprso.Configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Permite CORS para todas las rutas
                .allowedOrigins("http://localhost:5173")  // Permite solo solicitudes desde el frontend React
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("*")  // Permite todos los encabezados
                .allowCredentials(true);  // Si necesitas enviar cookies o credenciales
    }
}
