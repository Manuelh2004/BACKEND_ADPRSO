package backend.backend_adprso.Controller.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import backend.backend_adprso.Controller.Response.ApiResponse;
import backend.backend_adprso.Entity.Usuario.UsuarioEntity;
import backend.backend_adprso.Service.Usuario.UsuarioService;

@RestController
@RequestMapping("/user/api/usuario")
public class UsuarioController {   
    @Autowired
    private UsuarioService usuarioService;    
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean verificarContraseña(String rawPassword, String storedPassword) {
    return bCryptPasswordEncoder.matches(rawPassword, storedPassword);
    }

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
