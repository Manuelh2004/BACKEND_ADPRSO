package backend.backend_adprso.Service.AuthService;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import backend.backend_adprso.Entity.Items.TipoUsuarioEntity;
import backend.backend_adprso.Entity.Usuario.PasswordResetTokenEntity;
import backend.backend_adprso.Entity.Usuario.UsuarioEntity;
import backend.backend_adprso.Repository.PasswordResetTokenRepository;
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
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public String login(String email, String password) {
        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findByUsrEmail(email);

        if (usuarioOpt.isPresent()) {
            UsuarioEntity usuario = usuarioOpt.get();
            String hashedPassword = usuario.getUsr_password(); // contraseña en la BD

            // Compara usando BCrypt
            if (passwordEncoder.matches(password, hashedPassword)) {
                String role = usuario.getTipoUsuario().getTipus_nombre();
                return jwtUtil.generateToken(email, role);
            }
        }

        throw new RuntimeException("Credenciales inválidas");

    }

    // Método para solicitar la recuperación de contraseña
    public void forgotPassword(String email) {
    System.out.println("Buscando correo: [" + email + "]");  // Verifica el correo que llega
    Optional<UsuarioEntity> usuario = usuarioRepository.findByUsrEmail(email);
    if (!usuario.isPresent()) {
        System.out.println("Correo no encontrado: [" + email + "]");  // Verifica el correo que no se encuentra
        throw new RuntimeException("El correo electrónico no está registrado.");
    }

        // Generar el token de recuperación
        String token = generateToken();

        // Establecer la fecha de expiración del token (por ejemplo, 1 hora)
        LocalDateTime expirationDate = LocalDateTime.now().plusHours(1);

         // Guardar el token en la base de datos
        PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
        passwordResetTokenEntity.setToken(token);
        passwordResetTokenEntity.setUsuario(usuario.get());
        passwordResetTokenEntity.setExpirationDate(expirationDate);

        passwordResetTokenRepository.save(passwordResetTokenEntity);

        // Enviar correo con el token
        String resetLink = "http://localhost:5173/reset-password?token=" + token;
        try {
            String subject = "Recuperación de Contraseña";
            String body = "<h1>Recuperación de Contraseña</h1>" +
                          "<p>Haz clic en el siguiente enlace para restablecer tu contraseña:</p>" +
                          "<p><a href='" + resetLink + "'>Restablecer contraseña</a></p>";
            emailService.sendEmail(email, subject, body);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo de recuperación", e);
        }
    }

    // Método para generar un token
    private String generateToken() {
        return java.util.UUID.randomUUID().toString();  // Token único usando UUID
    }

    // Método para restablecer la contraseña
    public void resetPassword(String token, String newPassword) {
    // Buscar el token en la base de datos
    Optional<PasswordResetTokenEntity> passwordResetToken = passwordResetTokenRepository.findByToken(token);
    
    if (!passwordResetToken.isPresent()) {
        throw new RuntimeException("Token inválido o expirado.");
    }

    PasswordResetTokenEntity resetToken = passwordResetToken.get();

    // Verificar si el token ha expirado
    if (resetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
        throw new RuntimeException("El token ha expirado.");
    }

    // Obtener el usuario asociado al token
    UsuarioEntity usuario = resetToken.getUsuario();
    
    // Hacer el hash de la nueva contraseña
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String hashedPassword = passwordEncoder.encode(newPassword);

    // Establecer la nueva contraseña hasheada en el usuario
    usuario.setUsr_password(hashedPassword);  // Guarda la contraseña de forma segura

    // Guardar el usuario con la nueva contraseña
    usuarioRepository.save(usuario);
    }

    public String register(UsuarioEntity usuario) {
        // Verificar si el usuario ya existe
        Optional<UsuarioEntity> existingUser = usuarioRepository.findByUsrEmail(usuario.getUsr_email());
        if (existingUser.isPresent()) {
            throw new RuntimeException("El correo electrónico ya está registrado.");
        }

        // Encriptar la contraseña antes de guardar
        String hashedPassword = passwordEncoder.encode(usuario.getUsr_password());
        usuario.setUsr_password(hashedPassword);

        // Obtener el tipo de usuario y asignarlo
        TipoUsuarioEntity tipoUsuario = tipoUsuarioRepository.findById(2L)
            .orElseThrow(() -> new RuntimeException("Tipo de usuario no encontrado"));
        usuario.setTipoUsuario(tipoUsuario);

        if (usuario.getUsr_email() == null) {
            usuario.setUsr_estado("activo");
        }

        usuarioRepository.save(usuario);

        String role = usuario.getTipoUsuario().getTipus_nombre();
        String token = jwtUtil.generateToken(usuario.getUsr_email(), role);

        // Enviar correo de bienvenida
        try {
            String subject = "¡Bienvenido al Sistema!";
            String body = "<h1>¡Bienvenido, " + usuario.getUsr_email() + "!</h1>" +
                        "<p>Gracias por registrarte en nuestro sistema. Estamos felices de tenerte como parte de nuestra comunidad.</p>" +
                        "<p>Tu token de acceso es: " + token + "</p>";
            emailService.sendEmail(usuario.getUsr_email(), subject, body);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo electrónico de bienvenida", e);
        }

        return token;
    }
}
