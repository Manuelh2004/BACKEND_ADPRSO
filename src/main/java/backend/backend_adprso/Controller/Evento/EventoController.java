package backend.backend_adprso.Controller.Evento;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.backend_adprso.Controller.Response.ApiResponse;
import backend.backend_adprso.Entity.Evento.EventoEntity;
import backend.backend_adprso.Service.AuthService.JwtUtil;
import backend.backend_adprso.Service.Evento.EventoService;

@RestController
@RequestMapping("/api/evento")
public class EventoController {
    @Autowired
    EventoService eventoService; 
    @Autowired
    private JwtUtil jwtUtil; // Dependencia de JwtUtil  

    @GetMapping("/activos/public")
    public ResponseEntity<ApiResponse<List<EventoEntity>>> listarEventosActivos() {
        List<EventoEntity> eventos = eventoService.ListarEventosActivos();
        return ResponseEntity.ok(
            new ApiResponse<>("success", 200, eventos, null)
        );
    }

    @GetMapping("/{id}/public")
    public ResponseEntity<ApiResponse<EventoEntity>> obtenerEventoPorId(@PathVariable Long id) {
        return eventoService.ObtenerEventoPorId(id)
            .map(evento -> ResponseEntity.ok(
                new ApiResponse<>("success", 200, evento, null)
            ))
            .orElse(ResponseEntity.status(404).body(
                new ApiResponse<>("error", 404, null, "Evento no encontrado")
            ));
    }

    /*********************************************************************************************** */
    /* Eventos asociado al usuario logueado */

    /* Guardar un usuario asociado a un evento */
    @PostMapping("/guardar/{eventoId}")
    public ResponseEntity<ApiResponse<Object>> guardarEventoUsuario(@PathVariable Long eventoId,
                                                                   @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");

            if (!jwtUtil.validateToken(token)) {
                ApiResponse<Object> response = new ApiResponse<>("error", HttpStatus.UNAUTHORIZED.value(), null, "Token no válido");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            eventoService.guardarEventoUsuario(eventoId, token);

            ApiResponse<Object> response = new ApiResponse<>("success", HttpStatus.OK.value(), null, "Evento y usuario guardados correctamente.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<Object> response = new ApiResponse<>("error", HttpStatus.BAD_REQUEST.value(), null, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /* Listar los eventos asociado a un usuario */
    @GetMapping("/mis-eventos")
    public ResponseEntity<ApiResponse<List<EventoEntity>>> listarEventosDelUsuario(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Extraer el token del encabezado Authorization
            String token = authorizationHeader.replace("Bearer ", "");

            // Verificar que el token sea válido
            if (!jwtUtil.validateToken(token)) {
                ApiResponse<List<EventoEntity>> response = new ApiResponse<>("error", HttpStatus.UNAUTHORIZED.value(), null, "Token no válido");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // Llamar al servicio para obtener los eventos del usuario logueado
            List<EventoEntity> eventos = eventoService.listarEventosDelUsuario(token);

            // Respuesta exitosa
            ApiResponse<List<EventoEntity>> response = new ApiResponse<>("success", HttpStatus.OK.value(), eventos, "Eventos obtenidos correctamente.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Respuesta con error
            ApiResponse<List<EventoEntity>> response = new ApiResponse<>("error", HttpStatus.BAD_REQUEST.value(), null, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
