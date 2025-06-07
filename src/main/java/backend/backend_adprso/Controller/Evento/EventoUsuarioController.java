package backend.backend_adprso.Controller.Evento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.backend_adprso.Controller.Response.ApiResponse;
import backend.backend_adprso.Entity.Evento.EventoUsuarioEntity;
import backend.backend_adprso.Service.Evento.EventoUsuarioService;

@RestController
@RequestMapping("/evento_usuario")
public class EventoUsuarioController {

    @Autowired
    private EventoUsuarioService eventoUsuarioService;

    // Endpoint para registrar la relación evento-usuario
    @PostMapping("/registrar")
    public ResponseEntity<ApiResponse<EventoUsuarioEntity>> registrarEventoUsuario(
            @RequestParam Long eventoId, 
            @RequestParam Long usuarioId) {

        try {
            // Registrar el evento-usuario y obtener la relación
            EventoUsuarioEntity eventoUsuario = eventoUsuarioService.registrarEventoUsuario(eventoId, usuarioId);
            return ResponseEntity.ok(
                new ApiResponse<>("success", 200, eventoUsuario, "Evento y Usuario registrados exitosamente.")
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(
                new ApiResponse<>("error", 400, null, e.getMessage())
            );
        }
    }
    
}
