package backend.backend_adprso.Service.Mascota;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.backend_adprso.Entity.Items.GustoEntity;
import backend.backend_adprso.Entity.Mascota.GustoMascotaEntity;
import backend.backend_adprso.Entity.Mascota.MascotaEntity;
import backend.backend_adprso.Repository.GustoMascotaRepository;
import backend.backend_adprso.Repository.GustoRepository;
import backend.backend_adprso.Repository.MascotaRepository;
import jakarta.transaction.Transactional;

@Service
public class MascotaService {
    @Autowired
    MascotaRepository mascotaRepository;
    @Autowired
    GustoMascotaRepository gustoMascotaRepository;
    @Autowired
    GustoRepository gustoRepository;

    public List<MascotaEntity> ListarMascotas() {
        return mascotaRepository.findAll();
    }

    public Optional<MascotaEntity> ObtenerMascotaPorId(Long id) {
        return mascotaRepository.findById(id);
    }
    
    @Transactional
    public MascotaEntity RegistrarMascota(MascotaEntity mascota, List<Long> gustosIds) {
        mascota.setMasc_estado(1);
        mascota.setMasc_fecha_registro(LocalDate.now());

        // Guardar la mascota primero para obtener el ID
        MascotaEntity mascotaGuardada = mascotaRepository.save(mascota);

        // Crear relaciones gusto-mascota
        List<GustoEntity> gustos = gustoRepository.findAllById(gustosIds);

        List<GustoMascotaEntity> gustosMascota = gustos.stream().map(gusto -> {
            GustoMascotaEntity gm = new GustoMascotaEntity();
            gm.setMasc_id(mascotaGuardada);
            gm.setGust_id(gusto);
            return gm;
        }).collect(Collectors.toList());

        // Guardar todas las relaciones
        gustoMascotaRepository.saveAll(gustosMascota);

        return mascotaGuardada;
    }
    

    public void EliminarMascota(Long id) {
        mascotaRepository.deleteById(id);
    }
    
    public List<MascotaEntity> ListarMascotasActivas() {
        return mascotaRepository.findByMascEstado(1);        
    }

    // Filtros ***********************
    public List<MascotaEntity> filtrarPorSexo(Long sexId) {
        return mascotaRepository.findBySexo(sexId);
    }

    public List<MascotaEntity> filtrarPorTamanio(Long tamId) {
        return mascotaRepository.findByTamanio(tamId);
    }

    public List<MascotaEntity> filtrarPorNivelEnergia(Long nienId) {
        return mascotaRepository.findByNivelEnergia(nienId);
    }

    public List<MascotaEntity> filtrarPorTipoMascota(Long tipmaId) {
        return mascotaRepository.findByTipoMascota(tipmaId);
    }

    // Método para filtrar con múltiples parámetros opcionales

    public List<MascotaEntity> filtrarPorFiltros(Long sexId, Long tamId, Long nienId, Long tipmaId) {
        return mascotaRepository.findByFilters(sexId, tamId, nienId, tipmaId);
    }
}
