package backend.backend_adprso.Controller.Auth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.backend_adprso.Controller.Response.ApiResponse;
import backend.backend_adprso.Entity.Usuario.UsuarioEntity;
import backend.backend_adprso.Service.AuthService.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginRequest request) {
        try {
            // Llamamos al servicio de login para obtener el token
            String token = authService.login(request.getEmail(), request.getPassword());

            // Creamos la respuesta con el token
            JwtResponse jwtResponse = new JwtResponse(token);
            ApiResponse<JwtResponse> response = new ApiResponse<>("success", HttpStatus.OK.value(), jwtResponse, "Login exitoso");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Si hay un error en el login, devolvemos un mensaje adecuado
            ApiResponse<String> response = new ApiResponse<>("error", HttpStatus.UNAUTHORIZED.value(), null, "Credenciales inválidas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

      @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody UsuarioEntity usuario) {
        try {
            // Llamamos al servicio para registrar el usuario y obtener el token
            String token = authService.register(usuario);  // Ahora también obtenemos el token

            // Creamos la respuesta con el usuario y el token
            ApiResponse<Map <String, Object>> response = new ApiResponse<>(
                "success",
                HttpStatus.CREATED.value(),
                Map.of("usuario", usuario, "token", token),  // Incluimos tanto el usuario como el token
                "Usuario registrado exitosamente"
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            // Si ocurre un error, devolvemos un mensaje de error con el código adecuado
            ApiResponse<UsuarioEntity> response = new ApiResponse<>("error", HttpStatus.BAD_REQUEST.value(), null, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
