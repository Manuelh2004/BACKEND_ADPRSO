package backend.backend_adprso.Controller.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.backend_adprso.Controller.Response.ApiResponse;
import backend.backend_adprso.Entity.Usuario.UsuarioEntity;
import backend.backend_adprso.Service.Usuario.UsuarioService;

@RestController
@RequestMapping("/user/api/usuario")
public class UsuarioController {   
    @Autowired
    private UsuarioService usuarioService;    

    @PutMapping("/{id}")
    public ApiResponse<UsuarioEntity> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioEntity usuario) {
        UsuarioEntity usuarioActualizado = usuarioService.actualizarUsuario(id, usuario);
        if (usuarioActualizado != null) {
            return new ApiResponse<>("success", 200, usuarioActualizado, "Usuario actualizado correctamente.");
        } else {
            return new ApiResponse<>("error", 404, null, "Usuario no encontrado.");
        }
    }
    
    
}
