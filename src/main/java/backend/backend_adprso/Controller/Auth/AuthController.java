package backend.backend_adprso.Controller.Auth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.backend_adprso.Controller.Response.ApiResponse;
import backend.backend_adprso.Entity.Usuario.ForgotPasswordRequest;
import backend.backend_adprso.Entity.Usuario.UsuarioEntity;
import backend.backend_adprso.Service.AuthService.AuthService;
import backend.backend_adprso.Service.Usuario.ResetPasswordRequest;
import backend.backend_adprso.Repository.PasswordResetTokenRepository;
import backend.backend_adprso.Repository.UsuarioRepository; 


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;


    @PostMapping("/forgot_password")
    public ResponseEntity<ApiResponse<?>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            // Llamamos al servicio de recuperación de contraseña con el correo del DTO
            String email = request.getEmail();
            authService.forgotPassword(email);
            ApiResponse<String> response = new ApiResponse<>(
                "success", 
                HttpStatus.OK.value(), 
                null, 
                "Correo de recuperación enviado con éxito."
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>(
                "error", 
                HttpStatus.BAD_REQUEST.value(), 
                null, 
                e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

   @PostMapping("/reset_password")
    public ResponseEntity<ApiResponse<?>> resetPassword(@RequestParam String token, @RequestBody ResetPasswordRequest resetPasswordRequest) {
    try {
        // Llamamos al servicio de recuperación de contraseña pasando solo la nueva contraseña
        authService.resetPassword(token, resetPasswordRequest.getNewPassword());

        ApiResponse<String> response = new ApiResponse<>(
            "success", 
            HttpStatus.OK.value(), 
            null, 
            "Contraseña restablecida correctamente."
        );
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        ApiResponse<String> response = new ApiResponse<>(
            "error", 
            HttpStatus.BAD_REQUEST.value(), 
            null, 
            e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    }

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

      @PostMapping("/crear_usuario")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody UsuarioEntity usuario) {
        try {
            String token = authService.register(usuario);  

            ApiResponse<Map <String, Object>> response = new ApiResponse<>(
                "success",
                HttpStatus.CREATED.value(),
                Map.of("usuario", usuario, "token", token),  
                "Usuario registrado exitosamente"
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            ApiResponse<UsuarioEntity> response = new ApiResponse<>("error", HttpStatus.BAD_REQUEST.value(), null, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
