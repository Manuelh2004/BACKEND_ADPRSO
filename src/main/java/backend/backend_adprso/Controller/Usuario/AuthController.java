package backend.backend_adprso.Controller.Usuario;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.backend_adprso.Entity.Usuario.EmpleadoEntity;
import backend.backend_adprso.Service.AuthService.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    public static class LoginRequest {
        public String email;
        public String password;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<EmpleadoEntity> empleado = authService.login(request.email, request.password);
        if (empleado.isPresent()) {
            // No se recomienda enviar la contraseña en la respuesta
            EmpleadoEntity usuario = empleado.get();
            usuario.setEmp_password(null);
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }
}
