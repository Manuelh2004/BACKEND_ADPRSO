package backend.backend_adprso.Controller.Adopcion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import backend.backend_adprso.Controller.Response.ApiResponse;
import backend.backend_adprso.Entity.Adopcion.AdopcionEntity;
import backend.backend_adprso.Entity.Mascota.MascotaEntity;
import backend.backend_adprso.Service.Adopcion.AdopcionService;
import backend.backend_adprso.Service.AuthService.JwtUtil;

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
                                                               @RequestBody AdopcionEntity adopcionEntity) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");

            if (!jwtUtil.validateToken(token)) {
                ApiResponse<Object> response = new ApiResponse<>("error", HttpStatus.UNAUTHORIZED.value(), null, "Token no válido");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            MascotaEntity mascota = new MascotaEntity();
            mascota.setMasc_id(mascotaId);
            adopcionEntity.setMascota(mascota);

            adopcionService.guardarAdopcion(adopcionEntity, token);

            ApiResponse<Object> response = new ApiResponse<>("success", HttpStatus.OK.value(), null, "Adopción registrada correctamente.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<Object> response = new ApiResponse<>("error", HttpStatus.BAD_REQUEST.value(), null, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }   
}
