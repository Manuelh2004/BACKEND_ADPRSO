package backend.backend_adprso.Controller.Evento;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.backend_adprso.Controller.Response.ApiResponse;
import backend.backend_adprso.Entity.Evento.EventoEntity;
import backend.backend_adprso.Service.Evento.EventoService;

@RestController
@RequestMapping("/evento")
public class EventoController {
    @Autowired
    EventoService eventoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EventoEntity>>> listarEventos() {
        List<EventoEntity> eventos = eventoService.ListarEventos();
        return ResponseEntity.ok(
            new ApiResponse<>("success", 200, eventos, null)
        );
    }

   @GetMapping("/activos")
    public ResponseEntity<ApiResponse<List<EventoEntity>>> listarEventosActivos() {
        List<EventoEntity> eventos = eventoService.ListarEventosActivos();
        return ResponseEntity.ok(
            new ApiResponse<>("success", 200, eventos, null)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventoEntity>> obtenerEventoPorId(@PathVariable Long id) {
        return eventoService.ObtenerEventoPorId(id)
            .map(evento -> ResponseEntity.ok(
                new ApiResponse<>("success", 200, evento, null)
            ))
            .orElse(ResponseEntity.status(404).body(
                new ApiResponse<>("error", 404, null, "Evento no encontrado")
            ));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EventoEntity>> registrarEvento(@RequestBody EventoEntity evento) {
        EventoEntity creado = eventoService.RegistrarEvento(evento);
        return ResponseEntity.status(201).body(
            new ApiResponse<>("success", 201, creado, "Evento creado exitosamente")
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarEvento(@PathVariable Long id) {
        if (eventoService.ObtenerEventoPorId(id).isPresent()) {
            eventoService.EliminarEvento(id);
            return ResponseEntity.ok(
                new ApiResponse<>("success", 200, null, "Evento eliminado correctamente")
            );
        } else {
            return ResponseEntity.status(404).body(
                new ApiResponse<>("error", 404, null, "Evento no encontrado")
            );
        }
    }
}
