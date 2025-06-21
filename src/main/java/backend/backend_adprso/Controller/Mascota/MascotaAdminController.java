package backend.backend_adprso.Controller.Mascota;

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
import backend.backend_adprso.Entity.Mascota.MascotaConGustosDTO;
import backend.backend_adprso.Entity.Mascota.MascotaEntity;
import backend.backend_adprso.Service.Mascota.MascotaService;

@RestController
@RequestMapping("/admin/api/mascota")
public class MascotaAdminController {
    @Autowired
    private MascotaService mascotaService; 

    @GetMapping("/listar_mascota")
    public ResponseEntity<ApiResponse<List<MascotaEntity>>> listarMascotas() {
        List<MascotaEntity> mascotas = mascotaService.listarMascotas();
        ApiResponse<List<MascotaEntity>> response = new ApiResponse<>("success", 200, mascotas, "Lista de mascotas");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registrar_mascota")
    public ResponseEntity<ApiResponse<MascotaEntity>> registrarMascota(@RequestBody MascotaConGustosDTO request) {
        // Llamamos al servicio pasando las URLs de las imágenes también
        MascotaEntity mascotaGuardada = mascotaService.RegistrarMascota(request.getMascota(), request.getGustosIds(), request.getImagenUrls());

        ApiResponse<MascotaEntity> response = new ApiResponse<>("success", 201, mascotaGuardada, "Mascota registrada correctamente con gustos e imágenes");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

   @PutMapping("/{id}")
    public ResponseEntity<MascotaEntity> updateMascota(@PathVariable Long id, @RequestBody MascotaUpdateRequest request) {
        try {
            // Llamamos al servicio pasando el ID de la mascota, los detalles y los nuevos gustos/imágenes
            MascotaEntity updatedMascota = mascotaService.updateMascota(id, request.getMascota(), request.getImagenUrls(), request.getNewGustos());
            return new ResponseEntity<>(updatedMascota, HttpStatus.OK);
        } catch (RuntimeException e) {
            // En caso de que no se encuentre la mascota o algún error en el proceso
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<MascotaEntity>> cambiarEstado(
        @PathVariable Long id, 
        @RequestParam Integer nuevoEstado) {
        
        MascotaEntity mascota = mascotaService.cambiarEstadoMascota(id, nuevoEstado);
        
        if (mascota != null) {
            return ResponseEntity.ok(
                new ApiResponse<>("success", 200, mascota, "Estado del evento actualizado exitosamente")
            );
        } else {
            return ResponseEntity.status(404).body(
                new ApiResponse<>("error", 404, null, "Evento no encontrado")
            );
        }
    }    
}
