package backend.backend_adprso.Service.Usuario;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.backend_adprso.Entity.Items.TipoUsuarioEntity;
import backend.backend_adprso.Entity.Usuario.UsuarioEntity;
import backend.backend_adprso.Repository.UsuarioRepository;

@Service
public class UsuarioService {    
   @Autowired
    UsuarioRepository usuarioRepository;

    public UsuarioEntity registrarUsuario(UsuarioEntity usuario) {
        // Aquí puedes validar si el email ya existe o cualquier otro campo

        // Establecer el estado por defecto o cualquier otra configuración adicional
        usuario.setUsr_estado("activo");  // Ejemplo: estado por defecto

        // Guarda el usuario en la base de datos
        return usuarioRepository.save(usuario);
    }

   public UsuarioEntity registrarUsuarioAdministrador(UsuarioEntity usuario) {
    // Asignar el tipo de usuario predeterminado como administrador (tipus_id = 1)
    if (usuario.getTipoUsuario() == null) {
        TipoUsuarioEntity tipoAdministrador = new TipoUsuarioEntity();
        tipoAdministrador.setTipus_id(1L);  // Usar '1L' para asignar un Long (1L es un valor Long)
        usuario.setTipoUsuario(tipoAdministrador);  // Asignar el tipo de usuario predeterminado
    }

    // Verificar si el tipo de usuario es "administrador" (id = 1)
    if (usuario.getTipoUsuario().getTipus_id() != 1L) {  // Comparar con 1L, que es un Long
        throw new IllegalArgumentException("Solo se pueden registrar usuarios de tipo administrador.");
    }

    // Establecer el estado por defecto o cualquier otra configuración adicional
    usuario.setUsr_estado("activo");  // Ejemplo: estado por defecto

    // Guarda el usuario en la base de datos
    return usuarioRepository.save(usuario);
    }

   public UsuarioEntity desactivarUsuario(Long id) {
    // Buscar el usuario por su id
    UsuarioEntity usuario = usuarioRepository.findById(id).orElse(null);

    if (usuario == null) {
        throw new IllegalArgumentException("Usuario no encontrado con id: " + id);
    }

    // Alternar el estado entre 'activo' y 'desactivado'
    if ("activo".equals(usuario.getUsr_estado())) {
        usuario.setUsr_estado("desactivado");  // Si está activo, se desactiva
    } else {
        usuario.setUsr_estado("activo");  // Si está desactivado, se activa nuevamente
    }

    // Guardar el usuario con el nuevo estado
    return usuarioRepository.save(usuario);
    }

    public List<UsuarioEntity> listarUsuariosPorTipo(Long tipus_id) {
        return usuarioRepository.findByTipoUsuario_Tipus_id(tipus_id);
    }
}
