package backend.backend_adprso.Service.Mascota;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import backend.backend_adprso.Entity.Mascota.MascotaEntity;
import backend.backend_adprso.Repository.MascotaRepository;

@Service
public class MascotaService {
    @Autowired
    MascotaRepository mascotaRepository;

    public List<MascotaEntity> ListarMascotas() {
        return mascotaRepository.findAll();
    }

    public Optional<MascotaEntity> ObtenerMascotaPorId(Long id) {
        return mascotaRepository.findById(id);
    }

    public MascotaEntity RegistrarMascota(MascotaEntity mascota) {
        // Por ejemplo, si quieres inicializar el estado en 1 al crear
        mascota.setMasc_estado(1);
        return mascotaRepository.save(mascota);
    }

    public void EliminarMascota(Long id) {
        mascotaRepository.deleteById(id);
    }

    // Puedes agregar métodos personalizados, por ejemplo, listar mascotas activas si tienes campo estado
    public List<MascotaEntity> ListarMascotasActivas() {
        return mascotaRepository.findByMascEstado(1); 
        // Este método requiere que agregues findByMascEstado en el repositorio
    }
}
