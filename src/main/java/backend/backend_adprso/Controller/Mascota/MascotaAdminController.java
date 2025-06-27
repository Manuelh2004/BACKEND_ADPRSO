package backend.backend_adprso.Controller.Mascota;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
    /*

     @GetMapping("/descargarExcel")
    public ResponseEntity<?> descargarExcelMascotas() {
        try {
            // Llamar al servicio para generar el archivo Excel
            mascotaService.generarExcelMascotas();

            // Crear el archivo para enviarlo
            File file = new File("mascotas.xlsx");

            // Verificar si el archivo existe
            if (!file.exists()) {
                return new ResponseEntity<>("El archivo no se pudo generar", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // Configurar los encabezados HTTP para la respuesta
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=mascotas.xlsx");

            // Leer el archivo como flujo de entrada
            FileInputStream fileInputStream = new FileInputStream(file);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileInputStream);
        } catch (IOException e) {
            return new ResponseEntity<>("Error al generar el archivo Excel", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

     */
}
