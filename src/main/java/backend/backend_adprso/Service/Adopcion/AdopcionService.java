package backend.backend_adprso.Service.Adopcion;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import backend.backend_adprso.Entity.Adopcion.AdopcionRequestDTO;
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

    public void guardarAdopcion(AdopcionRequestDTO adopcionRequestDTO, String token) {
        UsuarioEntity usuarioLogueado = usuarioService.obtenerUsuarioLogueado(token);

        boolean existeAdopcion = adopcionRepository.existsByUsuarioAndMascota(usuarioLogueado, adopcionRequestDTO.getMascota());
        if (existeAdopcion) {
            throw new RuntimeException("El usuario ya ha enviado su solicitud de adopción para esta mascota");
        }

        MascotaEntity mascota = mascotaRepository.findById(adopcionRequestDTO.getMascota().getMasc_id())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        AdopcionEntity adopcionEntity = new AdopcionEntity();
        adopcionEntity.setMascota(mascota);
        adopcionEntity.setAdop_motivo(adopcionRequestDTO.getAdop_motivo());
        adopcionEntity.setUsuario(usuarioLogueado);
        adopcionEntity.setAdop_estado(0);
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

    // 0 - pendiente
    // 1 - aceptada
    // 2 - rechazada 
    @Transactional
    public AdopcionEntity cambiarEstadoAdopcion(Long adopId, Integer nuevoEstado) {
        Optional<AdopcionEntity> adopcionExistente = adopcionRepository.findById(adopId);        

        if (adopcionExistente.isPresent()) {
            AdopcionEntity adopcion = adopcionExistente.get();
            MascotaEntity mascota = adopcion.getMascota(); 

            if (nuevoEstado == 1) {
                if (mascota.getMasc_estado() == 1) {
                    mascota.setMasc_estado(0); 
                }
            } 
           
            else if (nuevoEstado == 2) {             
                if (mascota.getMasc_estado() == 0) {
                }
            }

            mascotaRepository.save(mascota);  

            adopcion.setAdop_estado(nuevoEstado); 
            return adopcionRepository.save(adopcion); 
        } else {  
            return null;  
        } 
    } 
    
    @Transactional
    public AdopcionEntity VolverAPendiente(Long adopId) {
        Optional<AdopcionEntity> adopcionExistente = adopcionRepository.findById(adopId);       

        if (adopcionExistente.isPresent()) {
            AdopcionEntity adopcion = adopcionExistente.get();              
            MascotaEntity mascota = adopcion.getMascota();  

            adopcion.setAdop_estado(0); 
            mascota.setMasc_estado(1); 

            mascotaRepository.save(mascota);                    
            return adopcionRepository.save(adopcion); 
        } else {  
            return null;  
        } 
    } 

    public List<AdopcionEntity> listarAdopcionesDelUsuario(String token) {
        UsuarioEntity usuarioLogueado = usuarioService.obtenerUsuarioLogueado(token);
        return adopcionRepository.findByUsuario(usuarioLogueado);
    }
}
