package backend.backend_adprso.Service.Usuario;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.backend_adprso.Entity.Usuario.EmpleadoEntity;
import backend.backend_adprso.Entity.Usuario.InteresadoEntity;
import backend.backend_adprso.Entity.Usuario.UsuarioEntity;
import backend.backend_adprso.Repository.EmpleadoRepository;
import backend.backend_adprso.Repository.InteresadoRepository;
import backend.backend_adprso.Repository.UsuarioRepository;


@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;
    
    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private InteresadoRepository interesadoRepository;


    public List<UsuarioEntity> ListarUsuario() {
        return usuarioRepository.findAll();
    }

     public EmpleadoEntity guardarEmpleado(EmpleadoEntity empleado) {
        return empleadoRepository.save(empleado);
    }

    public InteresadoEntity guardarInteresado(InteresadoEntity interesado) {
        return interesadoRepository.save(interesado);
    }
}
