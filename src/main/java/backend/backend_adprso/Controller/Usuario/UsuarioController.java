package backend.backend_adprso.Controller.Usuario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.backend_adprso.Controller.Response.ApiResponse;
import backend.backend_adprso.Entity.Usuario.UsuarioEntity;
import backend.backend_adprso.Service.Usuario.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
   
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/crear_cuenta")
    public ResponseEntity<ApiResponse<UsuarioEntity>> registrarUsuario(@RequestBody UsuarioEntity usuario) {
        // Llama al servicio para registrar el usuario
        UsuarioEntity usuarioCreado = usuarioService.registrarUsuario(usuario);

        if (usuarioCreado != null) {
            return ResponseEntity.status(201).body(
                new ApiResponse<>("success", 201, usuarioCreado, "Usuario registrado exitosamente")
            );
        } else {
            return ResponseEntity.status(400).body(
                new ApiResponse<>("error", 400, null, "No se pudo registrar el usuario")
            );
        }
    }

     @PostMapping("/registrar_administrador")
    public ResponseEntity<ApiResponse<UsuarioEntity>> registrarUsuarioAdministrador(@RequestBody UsuarioEntity usuario) {
        try {
            UsuarioEntity usuarioRegistrado = usuarioService.registrarUsuarioAdministrador(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>("success", 201, usuarioRegistrado, "Usuario administrador registrado exitosamente.")
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiResponse<>("error", 400, null, e.getMessage())
            );
        }
    }

    @PutMapping("/{id}/cambiar_estado")
    public ResponseEntity<ApiResponse<UsuarioEntity>> desactivarUsuario(@PathVariable Long id) {
        try {
            UsuarioEntity usuarioDesactivado = usuarioService.desactivarUsuario(id);
            return ResponseEntity.ok(
                new ApiResponse<>("success", 200, usuarioDesactivado, "Usuario desactivado exitosamente")
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiResponse<>("error", 404, null, e.getMessage())
            );
        }
    }
    
    @GetMapping("/listar_tipo/{tipus_id}")
    public ResponseEntity<ApiResponse<List<UsuarioEntity>>> listarUsuariosPorTipo(@PathVariable Long tipus_id) {
        List<UsuarioEntity> usuarios = usuarioService.listarUsuariosPorTipo(tipus_id);
        if (!usuarios.isEmpty()) {
            return ResponseEntity.ok(
                new ApiResponse<>("success", 200, usuarios, "Usuarios encontrados exitosamente")
            );
        } else {
            return ResponseEntity.status(404).body(
                new ApiResponse<>("error", 404, null, "No se encontraron usuarios para el tipo especificado")
            );
        }
    }
}
