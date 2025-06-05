package backend.backend_adprso.Controller.Planes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.backend_adprso.Entity.Planes.ApadrinamientoEntity;
import backend.backend_adprso.Service.Planes.ApadrinamientoService;

// FALTA *********************************************

@RestController
@RequestMapping("/apadrinamientos")
@CrossOrigin(origins = "*")
public class ApadrinamientoController {
    @Autowired
    private ApadrinamientoService apadrinamientoService;

    // Obtener todos los apadrinamientos
    @GetMapping
    public List<ApadrinamientoEntity> listarTodos() {
        return apadrinamientoService.listarTodos();
    }

    // Obtener un apadrinamiento por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApadrinamientoEntity> obtenerPorId(@PathVariable Long id) {
        return apadrinamientoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear un nuevo apadrinamiento
    @PostMapping
    public ApadrinamientoEntity crear(@RequestBody ApadrinamientoEntity apadrinamiento) {
        return apadrinamientoService.guardar(apadrinamiento);
    }   
    // Eliminar un apadrinamiento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (apadrinamientoService.buscarPorId(id).isPresent()) {
            apadrinamientoService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
