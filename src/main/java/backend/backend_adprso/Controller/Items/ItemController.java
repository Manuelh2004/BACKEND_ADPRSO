package backend.backend_adprso.Controller.Items;

import java.util.List;

import backend.backend_adprso.Entity.Items.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.backend_adprso.Controller.Response.ApiResponse;
import backend.backend_adprso.Service.Items.ItemService;

@RestController
@RequestMapping("/api/item")
public class ItemController {
    @Autowired
    ItemService itemService;

    @GetMapping("/estado_salud/public")
    public ResponseEntity<ApiResponse<List<EstadoSaludEntity>>> listarEstadoSalud() {
        List<EstadoSaludEntity> lista = itemService.ListarEstadoSalud();
        String mensaje = lista.isEmpty() ? "No se encontraron estados de salud" : null;
        return ResponseEntity.ok(new ApiResponse<>("success", 200, lista, mensaje));
    }

    @GetMapping("/estado_vacuna/public")
    public ResponseEntity<ApiResponse<List<EstadoVacunaEntity>>> listarEstadoVacuna() {
        List<EstadoVacunaEntity> lista = itemService.ListarEstadoVacuna();
        String mensaje = lista.isEmpty() ? "No se encontraron estados de vacuna" : null;
        return ResponseEntity.ok(new ApiResponse<>("success", 200, lista, mensaje));
    }

    @GetMapping("/gustos/public")
    public ResponseEntity<ApiResponse<List<GustoEntity>>> listarGustos() {
        List<GustoEntity> lista = itemService.ListarGustos();
        String mensaje = lista.isEmpty() ? "No se encontraron gustos" : null;
        return ResponseEntity.ok(new ApiResponse<>("success", 200, lista, mensaje));
    }

    @GetMapping("/nivel_energia/public")
    public ResponseEntity<ApiResponse<List<NivelEnergiaEntity>>> listarNivelEnergia() {
        List<NivelEnergiaEntity> lista = itemService.ListarNivelEnergia();
        String mensaje = lista.isEmpty() ? "No se encontraron niveles de energía" : null;
        return ResponseEntity.ok(new ApiResponse<>("success", 200, lista, mensaje));
    }

    @GetMapping("/tamanios/public")
    public ResponseEntity<ApiResponse<List<TamanioEntity>>> listarTamanios() {
        List<TamanioEntity> lista = itemService.ListarTamanios();
        String mensaje = lista.isEmpty() ? "No se encontraron tamaños" : null;
        return ResponseEntity.ok(new ApiResponse<>("success", 200, lista, mensaje));
    }

    @GetMapping("/tipo_usuario/public")
    public ResponseEntity<ApiResponse<List<TipoUsuarioEntity>>> listarTipoUsuario() {
        List<TipoUsuarioEntity> lista = itemService.ListarTipoUsuario();
        String mensaje = lista.isEmpty() ? "No se encontraron tipos de usuario" : null;
        return ResponseEntity.ok(new ApiResponse<>("success", 200, lista, mensaje));
    }

    @GetMapping("/tipo-documento/public")
    public ResponseEntity<ApiResponse<List<TipoDocumentoEntity>>> listarTipoDocumento() {
        List<TipoDocumentoEntity> lista = itemService.ListarTipoDocumento();
        String mensaje = lista.isEmpty() ? "No se encontraron tipos de usuario" : null;
        return ResponseEntity.ok(new ApiResponse<>("success", 200, lista, mensaje));
    }

    @GetMapping("/tipo_mascota/public")
    public ResponseEntity<ApiResponse<List<TipoMascotaEntity>>> listarTipoMascota() {
        List<TipoMascotaEntity> lista = itemService.ListarTipoMascota();
        String mensaje = lista.isEmpty() ? "No se encontraron tipos de mascotas" : null;
        return ResponseEntity.ok(new ApiResponse<>("success", 200, lista, mensaje));
    }


    @GetMapping("/sexo/public")
    public ResponseEntity<ApiResponse<List<SexoEntity>>> listarSexo() {
        List<SexoEntity> lista = itemService.ListarSexo();
        String mensaje = lista.isEmpty() ? "No se encontraron sexos" : null;
        System.out.println(lista);
        return ResponseEntity.ok(new ApiResponse<>("success", 200, lista, mensaje));
    }
}