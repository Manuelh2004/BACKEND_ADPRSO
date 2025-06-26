package backend.backend_adprso.Controller.Adopcion;

import backend.backend_adprso.Entity.Adopcion.AdopcionRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import backend.backend_adprso.Controller.Response.ApiResponse;
import backend.backend_adprso.Entity.Adopcion.AdopcionEntity;
import backend.backend_adprso.Service.Adopcion.AdopcionService;
import backend.backend_adprso.Service.AuthService.JwtUtil;
import java.util.List;

@RestController
@RequestMapping("/api/adopcion")
public class AdopcionController {
     @Autowired
    private AdopcionService adopcionService;

    @Autowired
    private JwtUtil jwtUtil; 

    @PostMapping("/guardar/{mascotaId}")
    public ResponseEntity<ApiResponse<Object>> guardarAdopcion(@PathVariable Long mascotaId,
                                                               @RequestHeader("Authorization") String authorizationHeader,
                                                               @RequestBody AdopcionRequestDTO adopcionRequestDTO) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");

            if (!jwtUtil.validateToken(token)) {
                ApiResponse<Object> response = new ApiResponse<>("error", HttpStatus.UNAUTHORIZED.value(), null, "Token no válido");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            adopcionService.guardarAdopcion(adopcionRequestDTO, token);

            ApiResponse<Object> response = new ApiResponse<>("success", HttpStatus.OK.value(), null, "Adopción registrada correctamente.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<Object> response = new ApiResponse<>("error", HttpStatus.BAD_REQUEST.value(), null, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @GetMapping("/mis-adopciones")
    public ResponseEntity<ApiResponse<List<AdopcionEntity>>> listarAdopcionesDelUsuario(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Extraer el token del encabezado Authorization
            String token = authorizationHeader.replace("Bearer ", "");

            // Verificar que el token sea válido
            if (!jwtUtil.validateToken(token)) {
                ApiResponse<List<AdopcionEntity>> response = new ApiResponse<>("error", HttpStatus.UNAUTHORIZED.value(), null, "Token no válido");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // Llamar al servicio para obtener los adopciones del usuario logueado
            List<AdopcionEntity> adopciones = adopcionService.listarAdopcionesDelUsuario(token);

            // Respuesta exitosa
            ApiResponse<List<AdopcionEntity>> response = new ApiResponse<>("success", HttpStatus.OK.value(), adopciones, "Adopciones obtenidos correctamente.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Respuesta con error
            ApiResponse<List<AdopcionEntity>> response = new ApiResponse<>("error", HttpStatus.BAD_REQUEST.value(), null, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
