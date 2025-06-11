package backend.backend_adprso.Service.AuthService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.backend_adprso.Entity.Items.TipoUsuarioEntity;
import backend.backend_adprso.Entity.Usuario.UsuarioEntity;
import backend.backend_adprso.Repository.TipoUsuarioRepository;
import backend.backend_adprso.Repository.UsuarioRepository;
import backend.backend_adprso.Service.EmailService;
import jakarta.mail.MessagingException;

@Service
public class AuthService {
    @Autowired
    private UsuarioRepository usuarioRepository; 
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository; 
    @Autowired
    private EmailService emailService;

    public String login(String email, String password) {
        Optional<UsuarioEntity> usuario = usuarioRepository.findByUsrEmail(email);
        if (usuario.isPresent() && usuario.get().getUsr_password().equals(password)) {
            // Obtener el rol del usuario
            String role = usuario.get().getTipoUsuario().getTipus_nombre();  // Esto te dará ROL_ADMIN o ROL_USER
            return jwtUtil.generateToken(email, role);
        } else {
            throw new RuntimeException("Credenciales inválidas");
        }
    }


    public String register(UsuarioEntity usuario) {
        // Verificar si el usuario ya existe
        Optional<UsuarioEntity> existingUser = usuarioRepository.findByUsrEmail(usuario.getUsr_email());
        if (existingUser.isPresent()) {
            throw new RuntimeException("El correo electrónico ya está registrado.");
        }

        // Obtener el tipo de usuario y asignarlo
        TipoUsuarioEntity tipoUsuario = tipoUsuarioRepository.findById(2L)
            .orElseThrow(() -> new RuntimeException("Tipo de usuario no encontrado"));
        usuario.setTipoUsuario(tipoUsuario);

        if (usuario.getUsr_email() == null) {
            usuario.setUsr_estado("activo");  // Establecer el estado como "activo" si es nulo
        }

        usuarioRepository.save(usuario);

        // Obtener el rol del nuevo usuario
        String role = usuario.getTipoUsuario().getTipus_nombre(); // Esto te dará ROL_ADMIN o ROL_USER

        // Generar el token JWT después de registrar al usuario, pasando el rol
        String token = jwtUtil.generateToken(usuario.getUsr_email(), role);

        // Enviar correo de bienvenida
        try {
            String subject = "¡Bienvenido al Sistema!";
            String body = "<h1>¡Bienvenido, " + usuario.getUsr_email() + "!</h1>" +
                        "<p>Gracias por registrarte en nuestro sistema. Estamos felices de tenerte como parte de nuestra comunidad.</p>" +
                        "<p>Tu token de acceso es: " + token + "</p>";  // Incluir el token en el correo de bienvenida
            emailService.sendEmail(usuario.getUsr_email(), subject, body);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo electrónico de bienvenida", e);
        }

        return token;  // Devolvemos el token
    }

}
