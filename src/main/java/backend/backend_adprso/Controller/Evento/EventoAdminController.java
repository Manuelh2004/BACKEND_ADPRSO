package backend.backend_adprso.Controller.Evento;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.backend_adprso.Controller.Response.ApiResponse;
import backend.backend_adprso.Entity.Evento.EventoEntity;
import backend.backend_adprso.Service.Evento.EventoService;

@RestController
@RequestMapping("/admin/api/evento")
public class EventoAdminController {
    @Autowired
    private EventoService eventoService; 

     @GetMapping("/listar_evento")
    public ResponseEntity<ApiResponse<List<EventoEntity>>> listarEventos() {
        List<EventoEntity> eventos = eventoService.ListarEventos();
        return ResponseEntity.ok(
            new ApiResponse<>("success", 200, eventos, null)
        );
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
}
