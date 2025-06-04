package backend.backend_adprso.Service.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import backend.backend_adprso.Entity.Items.TipoUsuarioEntity;
import backend.backend_adprso.Entity.Usuario.UsuarioEntity;
import backend.backend_adprso.Repository.TipoUsuarioRepository;
import backend.backend_adprso.Repository.UsuarioRepository;
import jakarta.transaction.Transactional;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder; 
    
  @Transactional
    public UsuarioEntity save(UsuarioEntity usuarioEntity) {

        // Buscar el tipo de usuario correcto según el rol
        TipoUsuarioEntity tipoUsuario = usuarioEntity.isAdmin()
            ? tipoUsuarioRepository.findByTipusNombre("Administrador").orElse(null)
            : tipoUsuarioRepository.findByTipusNombre("Usuario").orElse(null);

        // Validar si se encontró el tipo de usuario
        if (tipoUsuario != null) {
            usuarioEntity.setTipoUsuario(tipoUsuario);
        }

        // Encriptar la contraseña antes de guardar
        usuarioEntity.setUsr_password(passwordEncoder.encode(usuarioEntity.getUsr_password()));        

        // Guardar el usuario en el repositorio
        return usuarioRepository.save(usuarioEntity);
    }

    // Obtener todos los usuarios
    public List<UsuarioEntity> obtenerTodosUsuarios() {
        return usuarioRepository.findAll();
    }

   
    
}
