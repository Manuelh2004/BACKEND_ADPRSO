package backend.backend_adprso.Service.Usuario;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import backend.backend_adprso.Entity.Items.TipoUsuarioEntity;
import backend.backend_adprso.Entity.Usuario.UsuarioEntity;
import backend.backend_adprso.Repository.TipoUsuarioRepository;
import backend.backend_adprso.Repository.UsuarioRepository;
import backend.backend_adprso.Service.AuthService.JwtUtil;
import jakarta.transaction.Transactional;

@Service
public class UsuarioService {    
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    TipoUsuarioRepository tipoUsuarioRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public UsuarioService(@Autowired PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
}
    public List<UsuarioEntity> listarUsuarios() {
        return usuarioRepository.findAll();
    }

     public UsuarioEntity registrarUsuario(UsuarioEntity usuario) {        
        // Encriptar la contraseña antes de guardarla
        String passwordEncriptada = bCryptPasswordEncoder.encode(usuario.getUsr_password());
        usuario.setUsr_password(passwordEncriptada);

        TipoUsuarioEntity tipoUsuarioAdmin = tipoUsuarioRepository.findById(1L).orElse(null);
        if (tipoUsuarioAdmin != null) {       
            usuario.setTipoUsuario(tipoUsuarioAdmin);
        } else {
            throw new RuntimeException("El tipo de usuario ROL_ADMIN no existe.");
        }

        return usuarioRepository.save(usuario);
    }

    public UsuarioEntity actualizarUsuario(Long id, UsuarioEntity usuario) {
        if (usuarioRepository.existsById(id)) {
            usuario.setUsr_id(id); 
            // Si se actualiza la contraseña, encriptarla
            if (usuario.getUsr_password() != null) {
                String passwordEncriptada = bCryptPasswordEncoder.encode(usuario.getUsr_password());
                usuario.setUsr_password(passwordEncriptada);
            }
            return usuarioRepository.save(usuario);
        }
        return null;
    }


    public List<UsuarioEntity> buscarPorNombre(String nombre) {
        return usuarioRepository.buscarPorNombre(nombre);
    }

    @Transactional
    public UsuarioEntity cambiarEstadoUsuario(Long evenId, String nuevoEstado) {
        Optional<UsuarioEntity> usuarioExistente = usuarioRepository.findById(evenId);
        
        if (usuarioExistente.isPresent()) {
            UsuarioEntity usuario = usuarioExistente.get();
            usuario.setUsr_estado(nuevoEstado);
            return usuarioRepository.save(usuario);
        } else {
            return null; 
        }
    }

    //********************************************************* */
    public UsuarioEntity obtenerUsuarioLogueado(String token) {
        String username = jwtUtil.extractUsername(token);
        return usuarioRepository.findByUsrEmail(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // Método privado para validar token y extraerlo limpio
    public String extraerYValidarToken(String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        if (!jwtUtil.validateToken(token)) {
            throw new RuntimeException("Token no válido");
        }
        return token;
    }

    public UsuarioEntity editarPerfil( String token, UsuarioEntity usuario) {
        UsuarioEntity usuarioLogueado = obtenerUsuarioLogueado(token);
        usuarioLogueado.setUsr_telefono(usuario.getUsr_telefono());
        usuarioLogueado.setUsr_nombre(usuario.getUsr_nombre());
        usuarioLogueado.setUsr_direccion(usuario.getUsr_direccion());
        usuarioLogueado.setUsr_apellido(usuario.getUsr_apellido());
        usuarioRepository.save(usuarioLogueado);
        return null;
    }
}
