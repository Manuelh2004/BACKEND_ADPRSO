package backend.backend_adprso.Controller.Planes;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.backend_adprso.Controller.Response.ApiResponse;
import backend.backend_adprso.Entity.Planes.ApoyoEconomicoEntity;
import backend.backend_adprso.Service.Planes.ApoyoEconomicoService;

@RestController
@RequestMapping("/planes")
public class ApoyoEconomicoController {
    @Autowired
    private ApoyoEconomicoService apoyoService;

    // GET
    @GetMapping
    public ResponseEntity<ApiResponse<List<ApoyoEconomicoEntity>>> listarTodos() {
        List<ApoyoEconomicoEntity> lista = apoyoService.listarApoyoEconomico();
        String mensaje = lista.isEmpty() ? "No se encontraron apoyos económicos" : null;
        return ResponseEntity.ok(new ApiResponse<>("success", 200, lista, mensaje));
    }

    // GET - Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Optional<ApoyoEconomicoEntity>>> buscarPorId(@PathVariable Long id) {
        Optional<ApoyoEconomicoEntity> apoyo = apoyoService.buscarPorId(id);
        String mensaje = apoyo.isEmpty() ? "Apoyo económico no encontrado" : null;
        return ResponseEntity.ok(new ApiResponse<>("success", 200, apoyo, mensaje));
    }

    // POST - Agregar nuevo apoyo
    @PostMapping
    public ResponseEntity<ApiResponse<ApoyoEconomicoEntity>> agregar(@RequestBody ApoyoEconomicoEntity apoyo) {
        ApoyoEconomicoEntity creado = apoyoService.agregar(apoyo);
        return ResponseEntity.ok(new ApiResponse<>("success", 201, creado, "Apoyo económico creado"));
    }

    // DELETE - Eliminar apoyo
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> eliminar(@PathVariable Long id) {
        boolean eliminado = apoyoService.eliminar(id);
        String mensaje = eliminado ? "Apoyo económico eliminado correctamente" : "Apoyo económico no encontrado";
        return ResponseEntity.ok(new ApiResponse<>("success", 200, mensaje, null));
    }
}
