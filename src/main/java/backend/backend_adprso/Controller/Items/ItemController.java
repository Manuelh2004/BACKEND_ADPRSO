package backend.backend_adprso.Controller.Items;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.backend_adprso.Controller.Response.ApiResponse;
import backend.backend_adprso.Entity.Items.EstadoSaludEntity;
import backend.backend_adprso.Entity.Items.EstadoVacunaEntity;
import backend.backend_adprso.Entity.Items.GustoEntity;
import backend.backend_adprso.Entity.Items.NivelEnergiaEntity;
import backend.backend_adprso.Entity.Items.TamanioEntity;
import backend.backend_adprso.Entity.Items.TipoMascotaEntity;
import backend.backend_adprso.Entity.Items.TipoPlanEntity;
import backend.backend_adprso.Entity.Items.TipoUsuarioEntity;
import backend.backend_adprso.Service.Items.ItemService;

@RestController
@RequestMapping("/user/item")
public class ItemController {
    @Autowired
    ItemService itemService; 

    @GetMapping("/estado_salud")
    public ResponseEntity<ApiResponse<List<EstadoSaludEntity>>> listarEstadoSalud() {
        List<EstadoSaludEntity> lista = itemService.ListarEstadoSalud();
        String mensaje = lista.isEmpty() ? "No se encontraron estados de salud" : null;
        return ResponseEntity.ok(new ApiResponse<>("success", 200, lista, mensaje));
    }

    @GetMapping("/estado_vacuna")
    public ResponseEntity<ApiResponse<List<EstadoVacunaEntity>>> listarEstadoVacuna() {
        List<EstadoVacunaEntity> lista = itemService.ListarEstadoVacuna();
        String mensaje = lista.isEmpty() ? "No se encontraron estados de vacuna" : null;
        return ResponseEntity.ok(new ApiResponse<>("success", 200, lista, mensaje));
    }

    @GetMapping("/gustos")
    public ResponseEntity<ApiResponse<List<GustoEntity>>> listarGustos() {
        List<GustoEntity> lista = itemService.ListarGustos();
        String mensaje = lista.isEmpty() ? "No se encontraron gustos" : null;
        return ResponseEntity.ok(new ApiResponse<>("success", 200, lista, mensaje));
    }

    @GetMapping("/nivel_energia")
    public ResponseEntity<ApiResponse<List<NivelEnergiaEntity>>> listarNivelEnergia() {
        List<NivelEnergiaEntity> lista = itemService.ListarNivelEnergia();
        String mensaje = lista.isEmpty() ? "No se encontraron niveles de energía" : null;
        return ResponseEntity.ok(new ApiResponse<>("success", 200, lista, mensaje));
    }   

    @GetMapping("/tamanios")
    public ResponseEntity<ApiResponse<List<TamanioEntity>>> listarTamanios() {
        List<TamanioEntity> lista = itemService.ListarTamanios();
        String mensaje = lista.isEmpty() ? "No se encontraron tamaños" : null;
        return ResponseEntity.ok(new ApiResponse<>("success", 200, lista, mensaje));
    }

    @GetMapping("/tipo_usuario")
    public ResponseEntity<ApiResponse<List<TipoUsuarioEntity>>> listarTipoUsuario() {
        List<TipoUsuarioEntity> lista = itemService.ListarTipoUsuario();
        String mensaje = lista.isEmpty() ? "No se encontraron tipos de usuario" : null;
        return ResponseEntity.ok(new ApiResponse<>("success", 200, lista, mensaje));
    }

    @GetMapping("/tipo_mascota")
    public ResponseEntity<ApiResponse<List<TipoMascotaEntity>>> listarTipoMascota() {
        List<TipoMascotaEntity> lista = itemService.ListarTipoMascota();
        String mensaje = lista.isEmpty() ? "No se encontraron tipos de mascotas" : null;
        return ResponseEntity.ok(new ApiResponse<>("success", 200, lista, mensaje));
    }

    @GetMapping("/tipo_plan")
    public ResponseEntity<ApiResponse<List<TipoPlanEntity>>> listarTipoPlan() {
        List<TipoPlanEntity> lista = itemService.ListarTipoPlan();
        String mensaje = lista.isEmpty() ? "No se encontraron tipos de planes" : null;
        return ResponseEntity.ok(new ApiResponse<>("success", 200, lista, mensaje));
    }
}
