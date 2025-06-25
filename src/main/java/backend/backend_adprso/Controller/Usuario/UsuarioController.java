package backend.backend_adprso.Controller.Usuario;

import backend.backend_adprso.Service.AuthService.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import backend.backend_adprso.Controller.Response.ApiResponse;
import backend.backend_adprso.Entity.Usuario.UsuarioEntity;
import backend.backend_adprso.Service.Usuario.UsuarioService;

@RestController
@RequestMapping("/user/api/usuario")
public class UsuarioController {   
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/perfil")
    public ResponseEntity<ApiResponse<UsuarioEntity>> obtenerPerfil(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = usuarioService.extraerYValidarToken(authorizationHeader);
            UsuarioEntity usuario = usuarioService.obtenerUsuarioLogueado(token);
            return ResponseEntity.ok(new ApiResponse<>("success", 200, usuario, "Usuario obtenido correctamente."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ApiResponse<>("error", 401, null, e.getMessage())
            );
        }
    }

    @PutMapping("/actualizar")
    public ResponseEntity<ApiResponse<UsuarioEntity>> actualizarPerfil(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UsuarioEntity datosActualizados) {
        try {
            String token = usuarioService.extraerYValidarToken(authorizationHeader);
            UsuarioEntity usuarioActualizado = usuarioService.actualizarUsuario(token, datosActualizados);
            return ResponseEntity.ok(new ApiResponse<>("success", 200, usuarioActualizado, "Usuario actualizado correctamente."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>("error", 400, null, e.getMessage())
            );
        }
    }
}
