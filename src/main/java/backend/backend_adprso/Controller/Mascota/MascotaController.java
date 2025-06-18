package backend.backend_adprso.Controller.Mascota;

import java.util.List;
import java.util.Optional;

import backend.backend_adprso.Entity.Mascota.MascotaDetalleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.backend_adprso.Controller.Response.ApiResponse;
import backend.backend_adprso.Entity.Mascota.MascotaEntity;
import backend.backend_adprso.Entity.Mascota.MascotaImagenDTO;
import backend.backend_adprso.Service.Mascota.MascotaService;

@RestController
@RequestMapping("/api/mascota")
public class MascotaController {   
    @Autowired
    private MascotaService mascotaService;     

    @GetMapping("/{id}/public")
    public ResponseEntity<ApiResponse<MascotaEntity>> obtenerMascotaPorId(@PathVariable Long id) {
        Optional<MascotaEntity> mascotaOpt = mascotaService.ObtenerMascotaPorId(id);
        if (mascotaOpt.isPresent()) {
            ApiResponse<MascotaEntity> response = new ApiResponse<>("success", 200, mascotaOpt.get(), "Mascota encontrada");
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<MascotaEntity> response = new ApiResponse<>("error", 404, null, "Mascota no encontrada");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/detalle/{id}/public")
    public ResponseEntity<ApiResponse<MascotaDetalleDTO>> obtenerDetalleMascota(
            @PathVariable Long id) {

        return mascotaService.obtenerDetalleMascota(id)
                .map(dto -> ResponseEntity.ok(
                        new ApiResponse<>("success",
                                HttpStatus.OK.value(),
                                dto,
                                "Mascota encontrada")))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("error",
                                HttpStatus.NOT_FOUND.value(),
                                null,
                                "Mascota no encontrada")));
    }

    @GetMapping("/filter/public")
    public ResponseEntity<ApiResponse<List<MascotaImagenDTO>>> filtrarMascotas(
        @RequestParam(required = false) Long sexId,
        @RequestParam(required = false) Long tamId,
        @RequestParam(required = false) Long nienId,
        @RequestParam(required = false) Long tipmaId) {

        List<MascotaImagenDTO> mascotasConImagenes = mascotaService.filtrarPorFiltros(sexId, tamId, nienId, tipmaId);
        ApiResponse<List<MascotaImagenDTO>> response = new ApiResponse<>("success", 200, mascotasConImagenes, "Mascotas filtradas con imágenes");
        return ResponseEntity.ok(response);
    }

    // Endpoint para obtener solo el nombre de la mascota y las imágenes asociadas
    @GetMapping("/cards/public")
    public ResponseEntity<ApiResponse<List<MascotaImagenDTO>>> listarMascotasCards() {
        List<MascotaImagenDTO> mascotasConImagenes = mascotaService.listarMascotasCards();
        ApiResponse<List<MascotaImagenDTO>> response = new ApiResponse<>("success", 200, mascotasConImagenes, "Todas las mascotas con imágenes");
        return ResponseEntity.ok(response);
    }
}
