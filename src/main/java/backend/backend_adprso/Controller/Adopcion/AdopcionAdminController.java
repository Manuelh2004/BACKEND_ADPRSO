package backend.backend_adprso.Controller.Adopcion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.backend_adprso.Controller.Response.ApiResponse;
import backend.backend_adprso.Entity.Adopcion.AdopcionEntity;
import backend.backend_adprso.Entity.Mascota.MascotaEntity;
import backend.backend_adprso.Service.Adopcion.AdopcionService;
import backend.backend_adprso.Service.Mascota.MascotaService;

@RestController
@RequestMapping("/admin/api/adopciones")
public class AdopcionAdminController {
    @Autowired
    private AdopcionService adopcionService;
    @Autowired
    private MascotaService mascotaService; 

     // Método para obtener todas las adopciones
    @GetMapping("/listar_adopciones")
    public ApiResponse<List<AdopcionEntity>> obtenerAdopciones() {
        List<AdopcionEntity> adopciones = adopcionService.obtenerAdopciones();
        return new ApiResponse<>("success", 200, adopciones, "Listado de adopciones");
    }

    @GetMapping("/aceptadas")
     public ResponseEntity<ApiResponse<List<AdopcionEntity>>> listarAdopcioneAceptadas() {
        List<AdopcionEntity> adopciones = adopcionService.listarAdopcionesAceptadas();
        return ResponseEntity.ok(
            new ApiResponse<>("success", 200, adopciones, null)
        );
    }

    @GetMapping("/pendientes")
    public ResponseEntity<ApiResponse<List<AdopcionEntity>>> listarAdopcionePendientes() {
        List<AdopcionEntity> adopciones = adopcionService.listarAdopcionesPendientes();
        return ResponseEntity.ok(
            new ApiResponse<>("success", 200, adopciones, null)
        );
    }

     @GetMapping("/rechazadas")
    public ResponseEntity<ApiResponse<List<AdopcionEntity>>> listarAdopcioneRechazadas() {
        List<AdopcionEntity> adopciones = adopcionService.listarAdopcionesRechazadas();
        return ResponseEntity.ok(
            new ApiResponse<>("success", 200, adopciones, null)
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<AdopcionEntity> obtenerAdopcionPorId(@PathVariable Long id) {
        AdopcionEntity adopcion = adopcionService.obtenerAdopcionPorId(id);
        
        if (adopcion == null) {
            return new ApiResponse<>("error", 404, null, "Adopción no encontrada");
        }
        
        return new ApiResponse<>("success", 200, adopcion, "Adopción encontrada");
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<AdopcionEntity>> cambiarEstado(
        @PathVariable Long id, 
        @RequestParam Integer nuevoEstado) {
        
        AdopcionEntity adopcion = adopcionService.cambiarEstadoAdopcion(id, nuevoEstado);
        
        if (adopcion != null) {
            return ResponseEntity.ok(
                new ApiResponse<>("success", 200, adopcion, "Estado de la adopción actualizada exitosamente")
            );
        } else {
            return ResponseEntity.status(404).body(
                new ApiResponse<>("error", 404, null, "Adopción no encontrada")
            );
        }
    }    

    @GetMapping("/buscar")
    public ApiResponse<List<MascotaEntity>> buscarMascotasPorNombre(@RequestParam String nombre) {
        try {
            List<MascotaEntity> mascotas = mascotaService.buscarPorNombre(nombre);
            
            if (mascotas.isEmpty()) {
                return new ApiResponse<>("success", HttpStatus.OK.value(), null, "No se encontraron mascotas con ese nombre.");
            }            
            return new ApiResponse<>("success", HttpStatus.OK.value(), mascotas, "Mascotas encontradas.");
        } catch (Exception e) {
            return new ApiResponse<>("error", HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "Hubo un error al procesar la solicitud.");
        }
    }

    @PutMapping("/volver-a-pendiente/{adopId}")
    public ResponseEntity<AdopcionEntity> volverAPendiente(@PathVariable Long adopId) {
        AdopcionEntity adopcion = adopcionService.VolverAPendiente(adopId);

        if (adopcion != null) {
            return ResponseEntity.ok(adopcion); 
        } else {
            return ResponseEntity.notFound().build(); 
        }
    }
}
