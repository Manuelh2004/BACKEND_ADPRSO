package backend.backend_adprso.Controller.Mascota;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.backend_adprso.Controller.Response.ApiResponse;
import backend.backend_adprso.Entity.Mascota.MascotaConGustosDTO;
import backend.backend_adprso.Entity.Mascota.MascotaEntity;
import backend.backend_adprso.Service.Mascota.MascotaService;

@RestController
@RequestMapping("/mascota")
public class MascotaController {
    @Autowired
    MascotaService mascotaService; 

    @GetMapping
    public ResponseEntity<ApiResponse<List<MascotaEntity>>> listarMascotas() {
        List<MascotaEntity> mascotas = mascotaService.ListarMascotas();
        ApiResponse<List<MascotaEntity>> response = new ApiResponse<>("success", 200, mascotas, "Lista de mascotas");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
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

    @PostMapping
    public ResponseEntity<ApiResponse<MascotaEntity>> registrarMascota(@RequestBody MascotaConGustosDTO request) {
        MascotaEntity mascotaGuardada = mascotaService.RegistrarMascota(request.getMascota(), request.getGustosIds());
        ApiResponse<MascotaEntity> response = new ApiResponse<>("success", 201, mascotaGuardada, "Mascota registrada correctamente con gustos");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarMascota(@PathVariable Long id) {
        Optional<MascotaEntity> mascotaOpt = mascotaService.ObtenerMascotaPorId(id);
        if (mascotaOpt.isPresent()) {
            mascotaService.EliminarMascota(id);
            ApiResponse<Void> response = new ApiResponse<>("success", 200, null, "Mascota eliminada correctamente");
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<Void> response = new ApiResponse<>("error", 404, null, "Mascota no encontrada");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}