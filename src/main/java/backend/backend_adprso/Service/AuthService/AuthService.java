package backend.backend_adprso.Service.AuthService;

import java.util.Optional;

import backend.backend_adprso.Controller.Auth.RestablecerRequest;
import backend.backend_adprso.Controller.Auth.VerificarRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String login(String email, String password) {
        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findByUsrEmail(email);

        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario o contraseña incorrecta");
        }

        UsuarioEntity usuario = usuarioOpt.get();

        // Siempre verificar primero el estado de la cuenta
        if (!usuario.isVerificado()) {
            throw new RuntimeException("Debes verificar tu correo electrónico antes de iniciar sesión.");
        }

        // Verificar contraseña después
        String hashedPassword = usuario.getUsr_password();
        if (!passwordEncoder.matches(password, hashedPassword)) {
            throw new RuntimeException("Usuario o contraseña incorrecta");
        }

        String role = usuario.getTipoUsuario().getTipus_nombre();
        return jwtUtil.generateToken(email, role);
    }

    public String register(UsuarioEntity usuario) {
        // Verificar si el usuario ya existe
        Optional<UsuarioEntity> existingUser = usuarioRepository.findByUsrEmail(usuario.getUsr_email());
        if (existingUser.isPresent()) {
            throw new RuntimeException("El correo electrónico ya está registrado.");
        }

        // Encriptar la contraseña
        String hashedPassword = passwordEncoder.encode(usuario.getUsr_password());
        usuario.setUsr_password(hashedPassword);

        // Asignar tipo de usuario
        TipoUsuarioEntity tipoUsuario = tipoUsuarioRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("Tipo de usuario no encontrado"));
        usuario.setTipoUsuario(tipoUsuario);

        // Estado y verificación
        usuario.setUsr_estado("activo");
        usuario.setVerificado(false);

        // Generar código de verificación de 6 dígitos
        int codigo = (int) (Math.random() * 900000) + 100000;
        usuario.setCodigoVerificacion(codigo);

        // Guardar usuario
        usuarioRepository.save(usuario);

        // Enviar correo con código de verificación
        try {
            String subject = "Verifica tu cuenta";
            String body = "<h1>¡Bienvenido al sistema!</h1>"
                    + "<p>Gracias por registrarte. Para activar tu cuenta, ingresa el siguiente código:</p>"
                    + "<h2>" + codigo + "</h2>";
            emailService.sendEmail(usuario.getUsr_email(), subject, body);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo electrónico de verificación", e);
        }

        return "Registro exitoso. Revisa tu correo y verifica tu cuenta.";
    }

    public String verificarCuenta(VerificarRequest verificarRequest) {
        UsuarioEntity usuario = usuarioRepository.findByUsrEmail(verificarRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        if (usuario.isVerificado()) {
            return "La cuenta ya ha sido verificada.";
        }

        if (usuario.getCodigoVerificacion() != null && usuario.getCodigoVerificacion().equals(verificarRequest.getCodigo())) {
            usuario.setVerificado(true);
            usuario.setCodigoVerificacion(null); // Limpia el código
            usuarioRepository.save(usuario);
            return "Cuenta verificada correctamente. Ya puedes iniciar sesión.";
        } else {
            throw new RuntimeException("El código de verificación es incorrecto.");
        }
    }

    public String iniciarRecuperacionPassword(String email) {
        UsuarioEntity usuario = usuarioRepository.findByUsrEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        int codigoRecuperacion = (int) (Math.random() * 900000) + 100000;
        usuario.setCodigoRecuperacion(String.valueOf(codigoRecuperacion));
        usuarioRepository.save(usuario);

        try {
            String subject = "Restablecer contraseña";
            String body = "<h1>Solicitud de recuperación</h1>" +
                    "<p>Para restablecer tu contraseña, utiliza este código:</p>" +
                    "<h2>" + codigoRecuperacion + "</h2>";
            emailService.sendEmail(email, subject, body);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar correo de recuperación", e);
        }


        return "Se envió un código a tu correo para restablecer la contraseña.";
    }

    public String restablecerPassword(RestablecerRequest restablecerRequest) {
        UsuarioEntity usuario = usuarioRepository.findByUsrEmail(restablecerRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        if (usuario.getCodigoRecuperacion() == null) {
            throw new RuntimeException("No hay solicitud de recuperación activa.");
        }

        if (!restablecerRequest.getCodigo().equals(usuario.getCodigoRecuperacion())) {
            throw new RuntimeException("El código de recuperación es incorrecto.");
        }

        // Actualizar contraseña y limpiar código
        usuario.setUsr_password(passwordEncoder.encode(restablecerRequest.getNuevaPassword()));
        usuario.setCodigoRecuperacion(null);
        usuarioRepository.save(usuario);

        return "Contraseña actualizada correctamente.";
    }
}
