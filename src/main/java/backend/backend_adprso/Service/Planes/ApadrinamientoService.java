package backend.backend_adprso.Service.Planes;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.backend_adprso.Entity.Planes.ApadrinamientoEntity;
import backend.backend_adprso.Repository.ApadrinamientoRepository;

@Service
public class ApadrinamientoService {
    
    @Autowired
    private ApadrinamientoRepository apadrinamientoRepository;

    // Obtener todos los apadrinamientos
    public List<ApadrinamientoEntity> listarTodos() {
        return apadrinamientoRepository.findAll();
    }

    // Buscar por ID
    public Optional<ApadrinamientoEntity> buscarPorId(Long id) {
        return apadrinamientoRepository.findById(id);
    }

    // Guardar o actualizar
    public ApadrinamientoEntity guardar(ApadrinamientoEntity apadrinamiento) {
        return apadrinamientoRepository.save(apadrinamiento);
    }

    // Eliminar por ID
    public void eliminar(Long id) {
        apadrinamientoRepository.deleteById(id);
    }
}
