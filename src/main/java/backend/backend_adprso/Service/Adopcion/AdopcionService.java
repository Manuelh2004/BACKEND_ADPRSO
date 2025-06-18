package backend.backend_adprso.Service.Adopcion;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.backend_adprso.Entity.Adopcion.AdopcionEntity;
import backend.backend_adprso.Entity.Mascota.MascotaEntity;
import backend.backend_adprso.Entity.Usuario.UsuarioEntity;
import backend.backend_adprso.Repository.AdopcionRepository;
import backend.backend_adprso.Repository.MascotaRepository;
import backend.backend_adprso.Service.Usuario.UsuarioService;
import jakarta.transaction.Transactional;

@Service
public class AdopcionService {
    @Autowired
    private AdopcionRepository adopcionRepository;

    @Autowired
    private UsuarioService usuarioService; 

    @Autowired
    private MascotaRepository mascotaRepository; 

    public void guardarAdopcion(AdopcionEntity adopcionEntity, String token) {
        UsuarioEntity usuarioLogueado = usuarioService.obtenerUsuarioLogueado(token);

        boolean existeAdopcion = adopcionRepository.existsByUsuarioAndMascota(usuarioLogueado, adopcionEntity.getMascota());
        if (existeAdopcion) {
            throw new RuntimeException("El usuario ya ha adoptado esta mascota");
        }

        MascotaEntity mascota = mascotaRepository.findById(adopcionEntity.getMascota().getMasc_id())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        adopcionEntity.setUsuario(usuarioLogueado);
        adopcionEntity.setAdop_estado(1); 
        adopcionEntity.setAdop_fecha(LocalDate.now());

        adopcionRepository.save(adopcionEntity);
    }

    public List<AdopcionEntity> obtenerAdopciones() {
        return adopcionRepository.findAll();
    }

    // Obtener una adopción por ID
    public AdopcionEntity obtenerAdopcionPorId(Long id) {
        return adopcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adopción no encontrada"));
    }

    public List<AdopcionEntity> listarAdopcionesPendientes() {
        return adopcionRepository.findByEvenEstado(0); 
    }

    public List<AdopcionEntity> listarAdopcionesAceptadas() {
        return adopcionRepository.findByEvenEstado(1); 
    }    

    public List<AdopcionEntity> listarAdopcionesRechazadas() {
        return adopcionRepository.findByEvenEstado(2); 
    }

    @Transactional
    public AdopcionEntity cambiarEstadoAdopcion(Long evenId, Integer nuevoEstado) {
        Optional<AdopcionEntity> adopcionExistente = adopcionRepository.findById(evenId);
        
        if (adopcionExistente.isPresent()) {
            AdopcionEntity adopcion = adopcionExistente.get();
            adopcion.setAdop_estado(nuevoEstado);
            return adopcionRepository.save(adopcion);
        } else {
            return null; 
        }
    }





}
