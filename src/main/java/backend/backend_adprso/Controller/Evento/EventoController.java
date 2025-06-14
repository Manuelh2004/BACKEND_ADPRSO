package backend.backend_adprso.Controller.Evento;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.backend_adprso.Controller.Response.ApiResponse;
import backend.backend_adprso.Entity.Evento.EventoEntity;
import backend.backend_adprso.Entity.Evento.EventoUsuarioEntity;
import backend.backend_adprso.Service.Evento.EventoService;

@RestController
@RequestMapping("/api/evento")
public class EventoController {
    @Autowired
    EventoService eventoService;

    @GetMapping("/listar")
    public ResponseEntity<ApiResponse<List<EventoEntity>>> listarEventos() {
        List<EventoEntity> eventos = eventoService.ListarEventos();
        return ResponseEntity.ok(
            new ApiResponse<>("success", 200, eventos, null)
        );
    }

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

    @PostMapping("/registrar_evento")
    public ResponseEntity<ApiResponse<EventoEntity>> registrarEvento(@RequestBody EventoEntity evento) {
        EventoEntity creado = eventoService.RegistrarEvento(evento);
        return ResponseEntity.status(201).body(
            new ApiResponse<>("success", 201, creado, "Evento creado exitosamente")
        );
    }

     @PutMapping("/{id}") 
    public ResponseEntity<ApiResponse<EventoEntity>> actualizarEvento(
        @PathVariable Long id, 
        @RequestBody EventoEntity eventoActualizado) {
        EventoEntity evento = eventoService.ActualizarEvento(id, eventoActualizado);
        
        if (evento != null) {
            return ResponseEntity.ok(
                new ApiResponse<>("success", 200, evento, "Evento actualizado exitosamente")
            );
        } else {
            return ResponseEntity.status(404).body(
                new ApiResponse<>("error", 404, null, "Evento no encontrado")
            );
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<EventoEntity>> cambiarEstado(
        @PathVariable Long id, 
        @RequestParam Integer nuevoEstado) {
        
        EventoEntity evento = eventoService.cambiarEstadoEvento(id, nuevoEstado);
        
        if (evento != null) {
            return ResponseEntity.ok(
                new ApiResponse<>("success", 200, evento, "Estado del evento actualizado exitosamente")
            );
        } else {
            return ResponseEntity.status(404).body(
                new ApiResponse<>("error", 404, null, "Evento no encontrado")
            );
        }
    }    

    /*********************************************************************************************** */

    @PostMapping("/asignar_usuario")
    public ResponseEntity<ApiResponse<EventoUsuarioEntity>> registrarEventoUsuario(
            @RequestParam Long eventoId, 
            @RequestParam Long usuarioId) {

        try {
            // Registrar el evento-usuario y obtener la relación
            EventoUsuarioEntity eventoUsuario = eventoService.registrarEventoUsuario(eventoId, usuarioId);
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
