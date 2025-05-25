package backend.backend_adprso.Controller.Usuario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.backend_adprso.Controller.Response.ApiResponse;
import backend.backend_adprso.Entity.Usuario.EmpleadoEntity;
import backend.backend_adprso.Entity.Usuario.InteresadoEntity;
import backend.backend_adprso.Entity.Usuario.UsuarioEntity;
import backend.backend_adprso.Service.Usuario.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    @Autowired
    UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UsuarioEntity>>> ListarUsuario() {
        List<UsuarioEntity> usuarios = usuarioService.ListarUsuario();
        return ResponseEntity.ok(
            new ApiResponse<>("success", 200, usuarios, null)
        );
    }

      @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody UsuarioEntity usuario) {
        UsuarioEntity usuarioGuardado;

        if (usuario instanceof EmpleadoEntity) {
            usuarioGuardado = usuarioService.guardarEmpleado((EmpleadoEntity) usuario);

        } else if (usuario instanceof InteresadoEntity) {
            usuarioGuardado = usuarioService.guardarInteresado((InteresadoEntity) usuario);

        } else {
            return ResponseEntity.badRequest().body("Tipo de usuario no válido");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioGuardado);
    }

    
}
