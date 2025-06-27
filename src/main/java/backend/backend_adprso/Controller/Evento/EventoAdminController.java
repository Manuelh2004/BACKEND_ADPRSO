package backend.backend_adprso.Controller.Evento;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import backend.backend_adprso.Entity.Usuario.UsuarioEntity;
import backend.backend_adprso.Service.Evento.EventoService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/registrar-evento-imagen")
    public ResponseEntity<ApiResponse<EventoEntity>> registrarEventoConImagen(
            @RequestPart("evento") EventoEntity evento,
            @RequestPart("imagen") MultipartFile imagen) {
        try {
            EventoEntity creado = eventoService.RegistrarEventoConImagen(evento, imagen);
            return ResponseEntity.status(201).body(
                    new ApiResponse<>("success", 201, creado, "Evento creado exitosamente con imagen")
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>("error", 500, null, "Error al crear el evento con imagen")
            );
        }
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

    @PutMapping("/{id}/actualizar-evento")
    public ResponseEntity<ApiResponse<EventoEntity>> actualizarEventoConImagen(
            @PathVariable Long id,
            @RequestPart("evento") EventoEntity eventoActualizado,
            @RequestPart(name = "imagen", required = false) MultipartFile nuevaImagen) {
        try {
            EventoEntity actualizado = eventoService.ActualizarEventoConImagen(id, eventoActualizado, nuevaImagen);
            if (actualizado != null) {
                return ResponseEntity.ok(
                        new ApiResponse<>("success", 200, actualizado, "Evento actualizado con imagen correctamente")
                );
            } else {
                return ResponseEntity.status(404).body(
                        new ApiResponse<>("error", 404, null, "Evento no encontrado")
                );
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>("error", 500, null, "Error al actualizar evento con imagen")
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
    
     @GetMapping("/{eventoId}/usuarios")
    public ApiResponse<List<UsuarioEntity>> obtenerUsuariosPorEvento(@PathVariable Long eventoId) {
        try {           
            List<UsuarioEntity> usuarios = eventoService.obtenerUsuariosPorEvento(eventoId);        
           
            return new ApiResponse<>("success", HttpStatus.OK.value(), usuarios, "Usuarios obtenidos con éxito");
        } catch (Exception e) {           
            return new ApiResponse<>("error", HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "Hubo un error al obtener los usuarios");
        }
    }

    @GetMapping("/activos")
    public ResponseEntity<ApiResponse<List<EventoEntity>>> listarEventosActivos() {
        List<EventoEntity> evento = eventoService.ListarEventosActivos();
        return ResponseEntity.ok(
            new ApiResponse<>("success", 200, evento, null)
        );
    }
    @GetMapping("/inactivos")
    public ResponseEntity<ApiResponse<List<EventoEntity>>> listarEventosInactivos() {
        List<EventoEntity> evento = eventoService.listarEventosInactivos();
        return ResponseEntity.ok(
            new ApiResponse<>("success", 200, evento, null)
        );
    }

    @GetMapping("/buscar")
    public ApiResponse<List<EventoEntity>> buscarEventosPorNombre(@RequestParam String nombre) {
        List<EventoEntity> eventos = eventoService.buscarPorNombre(nombre);

        if (eventos.isEmpty()) {
            return new ApiResponse<>(
                "ERROR", HttpStatus.NOT_FOUND.value(),
                null, "No se encontraron eventos con ese nombre."
            );
        }
        return new ApiResponse<>(
            "SUCCESS", HttpStatus.OK.value(),
            eventos, "Eventos encontrados exitosamente."
        );
    }

    @PostMapping("/reporte-inscripciones")
    public ResponseEntity<byte[]> descargarReporteFiltrado(@RequestBody List<Long> idsEvento) {
        try {
            byte[] contenido = eventoService.generarReporteInscripciones(idsEvento);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.attachment().filename("reporte_inscripciones.xlsx").build());
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return new ResponseEntity<>(contenido, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
