package backend.backend_adprso.Controller.Mascota;

import java.util.List;
import java.util.Optional;

import backend.backend_adprso.Entity.Mascota.MascotaDetalleDTO;
import backend.backend_adprso.Entity.Mascota.MascotaRequestDTO;
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

    @PostMapping("/registrar")
    public ResponseEntity<ApiResponse<MascotaEntity>> registrarMascota(@RequestBody MascotaRequestDTO dto) {
        MascotaEntity mascotaGuardada = mascotaService.registrar(dto);
        ApiResponse<MascotaEntity> response = new ApiResponse<>("success", 201, mascotaGuardada, "Mascota registrada");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/registrar_mascota")
    public ResponseEntity<ApiResponse<MascotaEntity>> registrarMascota(@RequestBody MascotaConGustosDTO request) {
        // Llamamos al servicio pasando las URLs de las imágenes también
        MascotaEntity mascotaGuardada = mascotaService.RegistrarMascota(request.getMascota(), request.getGustosIds(), request.getImagenUrls());

        ApiResponse<MascotaEntity> response = new ApiResponse<>("success", 201, mascotaGuardada, "Mascota registrada correctamente con gustos e imágenes");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MascotaEntity>> actualizarMascota(
            @PathVariable Long id,
            @RequestBody MascotaEntity mascotaActualizada,
            @RequestParam(required = false) List<Long> nuevosGustosIds,
            @RequestParam(required = false) List<String> nuevasImagenUrls) {

        Optional<MascotaEntity> mascotaOpt = mascotaService.actualizarMascota(id, mascotaActualizada, nuevosGustosIds, nuevasImagenUrls);

        if (mascotaOpt.isPresent()) {
            MascotaEntity mascotaGuardada = mascotaOpt.get();
            ApiResponse<MascotaEntity> response = new ApiResponse<>(
                    "success", 200, mascotaGuardada, "Mascota actualizada correctamente"
            );
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<MascotaEntity> response = new ApiResponse<>(
                    "error", 404, null, "Mascota no encontrada"
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
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
