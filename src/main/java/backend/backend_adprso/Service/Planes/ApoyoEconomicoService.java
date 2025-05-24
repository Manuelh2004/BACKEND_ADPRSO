package backend.backend_adprso.Service.Planes;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.backend_adprso.Entity.Planes.ApoyoEconomicoEntity;
import backend.backend_adprso.Repository.ApoyoEconomicoRepository;

@Service
public class ApoyoEconomicoService {

    @Autowired
    private ApoyoEconomicoRepository apoyoEconomicoRepository;

    //Mostrar Datos
    public List<ApoyoEconomicoEntity> listarApoyoEconomico(){
        return apoyoEconomicoRepository.findAll();
    }

    //Agregar
    public ApoyoEconomicoEntity agregar(ApoyoEconomicoEntity apoyo) {
        return apoyoEconomicoRepository.save(apoyo);
    }

    // Eliminar
    public boolean eliminar(Long id) {
        if (apoyoEconomicoRepository.existsById(id)) {
            apoyoEconomicoRepository.deleteById(id);
            return true;
        }
        return false;
    }

     // Buscar por ID
     public Optional<ApoyoEconomicoEntity> buscarPorId(Long id) {
        return apoyoEconomicoRepository.findById(id);
    }
}
