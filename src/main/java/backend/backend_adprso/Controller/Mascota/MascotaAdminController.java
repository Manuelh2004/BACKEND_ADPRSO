package backend.backend_adprso.Controller.Mascota;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import backend.backend_adprso.Controller.Response.ApiResponse;
import backend.backend_adprso.Entity.Mascota.MascotaEntity;
import backend.backend_adprso.Service.Mascota.MascotaService;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<ApiResponse<MascotaEntity>> registrarMascota(
            @RequestPart("mascota") MascotaEntity mascota,
            @RequestPart("gustos") List<Long> gustosIds,
            @RequestPart(value = "imagenes", required = false) List<MultipartFile> imagenes) {

        MascotaEntity mascotaGuardada = mascotaService.RegistrarMascota(mascota, gustosIds, imagenes);

        ApiResponse<MascotaEntity> response = new ApiResponse<>("success", 201, mascotaGuardada, "Mascota registrada correctamente con imágenes y gustos");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MascotaEntity> updateMascota(
            @PathVariable Long id,
            @RequestPart("mascota") MascotaEntity mascota,
            @RequestPart("gustos") List<Long> gustosIds,
            @RequestPart(value = "imagenes", required = false) List<MultipartFile> nuevasImagenes,
            @RequestPart(value = "imagenesAEliminar", required = false) List<String> imagenesAEliminar
    ) {
        MascotaEntity updated = mascotaService.updateMascota(id, mascota, nuevasImagenes, gustosIds, imagenesAEliminar);
        return ResponseEntity.ok(updated);
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

    @GetMapping("/activos")
    public ResponseEntity<ApiResponse<List<MascotaEntity>>> listarEventosActivos() {
        List<MascotaEntity> mascota = mascotaService.ListarMascotasActivas();
        return ResponseEntity.ok(
            new ApiResponse<>("success", 200, mascota, null)
        );
    }

    @GetMapping("/inactivos")
    public ResponseEntity<ApiResponse<List<MascotaEntity>>> listarEventosInactivos() {
        List<MascotaEntity> mascota = mascotaService.listarMascotasInactivas();
        return ResponseEntity.ok(
            new ApiResponse<>("success", 200, mascota, null)
        );
    }

     @GetMapping("/exportar-excel")
    public ResponseEntity<ByteArrayResource> exportarExcel() throws IOException {
        // Generar el archivo Excel
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mascotaService.generarExcelMascotas(outputStream);
        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=mascotas.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }    
}
