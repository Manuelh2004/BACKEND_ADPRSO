package backend.backend_adprso.Service.Adopcion;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.backend_adprso.Entity.Adopcion.AdopcionEntity;
import backend.backend_adprso.Entity.Mascota.MascotaEntity;
import backend.backend_adprso.Entity.Usuario.UsuarioEntity;
import backend.backend_adprso.Repository.AdopcionRepository;
import backend.backend_adprso.Repository.MascotaRepository;
import backend.backend_adprso.Service.Usuario.UsuarioService;

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
}
