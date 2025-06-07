package backend.backend_adprso.Service.Items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.backend_adprso.Entity.Items.TipoUsuarioEntity;
import backend.backend_adprso.Repository.TipoUsuarioRepository;

@Service
public class TipoUsuarioService {
    
    @Autowired
    TipoUsuarioRepository TipoUsuarioRepository;

    // Método para obtener el tipo de usuario por ID
    public TipoUsuarioEntity getTipoUsuarioById(Long id) {
        return TipoUsuarioRepository.findById(id).orElse(null);  // Retorna el tipo de usuario o null si no se encuentra
    }
    
}
