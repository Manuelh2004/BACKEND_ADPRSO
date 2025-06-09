package backend.backend_adprso.Service.AuthService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.backend_adprso.Entity.Items.TipoUsuarioEntity;
import backend.backend_adprso.Entity.Usuario.UsuarioEntity;
import backend.backend_adprso.Repository.TipoUsuarioRepository;
import backend.backend_adprso.Repository.UsuarioRepository;

@Service
public class AuthService {
    @Autowired
    private UsuarioRepository usuarioRepository; 
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository; 

    public String login(String email, String password) {
        Optional<UsuarioEntity> usuario = usuarioRepository.findByUsrEmail(email);
        if (usuario.isPresent() && usuario.get().getUsr_password().equals(password)) {
            return jwtUtil.generateToken(email);
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

        TipoUsuarioEntity tipoUsuario = tipoUsuarioRepository.findById(2L).orElseThrow(() -> new RuntimeException("Tipo de usuario no encontrado"));      

        // Asignar el tipo de usuario por defecto (ID = 2)
        usuario.setTipoUsuario(tipoUsuario);

        // Si deseas establecer un estado predeterminado, por ejemplo, "activo":
        if (usuario.getUsr_email() == null) {
            usuario.setUsr_estado("activo");  // Establecer el estado como "activo" si es nulo
        }

        // Guardar el nuevo usuario en la base de datos
        usuarioRepository.save(usuario);

        return "Usuario registrado exitosamente!";
    }
}
