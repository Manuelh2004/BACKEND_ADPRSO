package backend.backend_adprso.Controller.Usuario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.backend_adprso.Controller.Response.ApiResponse;
import backend.backend_adprso.Entity.Usuario.UsuarioEntity;
import backend.backend_adprso.Service.Usuario.UsuarioService;

@RestController
@RequestMapping("/admin/api/usuario")
public class UsuarioAdminController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/listar_usuarios")
    public ApiResponse<List<UsuarioEntity>> listarUsuarios() {
        List<UsuarioEntity> usuarios = usuarioService.listarUsuarios();
        if (usuarios.isEmpty()) {
            return new ApiResponse<>("error", 404, null, "No se encontraron usuarios.");
        }
        return new ApiResponse<>("success", 200, usuarios, "Usuarios listados correctamente.");
    }

    @PostMapping("/registrar_usuario")
    public ApiResponse<UsuarioEntity> registrarUsuario(@RequestBody UsuarioEntity usuario) {
        try {
            UsuarioEntity usuarioGuardado = usuarioService.registrarUsuario(usuario);
            return new ApiResponse<>("success", 201, usuarioGuardado, "Usuario registrado correctamente.");
        } catch (RuntimeException e) {
            return new ApiResponse<>("error", 400, null, e.getMessage());
        }
    }

    @GetMapping("/buscar")
    public ApiResponse<List<UsuarioEntity>> buscarUsuariosPorNombre(@RequestParam String nombre) {
        List<UsuarioEntity> usuarios = usuarioService.buscarPorNombre(nombre);
        if (usuarios.isEmpty()) {
            return new ApiResponse<>("error", 404, null, "No se encontraron usuarios con ese nombre.");
        }
        return new ApiResponse<>("success", 200, usuarios, "Usuarios encontrados correctamente.");
    }

    @PutMapping("/{id}/estado")
    public ApiResponse<UsuarioEntity> cambiarEstadoUsuario(@PathVariable Long id, @RequestParam String nuevoEstado) {
        UsuarioEntity usuarioActualizado = usuarioService.cambiarEstadoUsuario(id, nuevoEstado);
        if (usuarioActualizado != null) {
            return new ApiResponse<>("success", 200, usuarioActualizado, "Estado del usuario cambiado correctamente.");
        } else {
            return new ApiResponse<>("error", 404, null, "Usuario no encontrado.");
        }
    }


   
}
