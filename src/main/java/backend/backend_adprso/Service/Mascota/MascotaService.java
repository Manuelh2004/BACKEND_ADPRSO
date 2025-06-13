package backend.backend_adprso.Service.Mascota;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.backend_adprso.Entity.Items.GustoEntity;
import backend.backend_adprso.Entity.Items.ImagenEntity;
import backend.backend_adprso.Entity.Mascota.GustoMascotaEntity;
import backend.backend_adprso.Entity.Mascota.MascotaEntity;
import backend.backend_adprso.Entity.Mascota.MascotaImagenDTO;
import backend.backend_adprso.Repository.GustoMascotaRepository;
import backend.backend_adprso.Repository.GustoRepository;
import backend.backend_adprso.Repository.ImagenRepository;
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
    @Autowired
    ImagenRepository imagenRepository;

    @Transactional
    public List<MascotaEntity> listarMascotas() {
        return mascotaRepository.findAll();       
    }

    @Transactional
    public Optional<MascotaEntity> ObtenerMascotaPorId(Long id) {
       Optional<MascotaEntity> mascotaOpt = mascotaRepository.findById(id);
        if (mascotaOpt.isPresent()) {
            MascotaEntity mascota = mascotaOpt.get();
            // Forzamos la carga de los gustos y las imágenes
            mascota.getGustoNames();  // Carga los gustos asociados
            List<ImagenEntity> imagenes = imagenRepository.findByMascotaId(id);  // Carga las imágenes
            mascota.setImagenes(imagenes);  // Asocia las imágenes a la mascota
            return Optional.of(mascota);
        }
        return Optional.empty();
    }
    
    @Transactional
    public MascotaEntity RegistrarMascota(MascotaEntity mascota, List<Long> gustosIds, List<String> imagenUrls) {
        mascota.setMasc_estado(1);
        mascota.setMasc_fecha_registro(LocalDate.now());

        MascotaEntity mascotaGuardada = mascotaRepository.save(mascota);

        List<GustoEntity> gustos = gustoRepository.findAllById(gustosIds);

        List<GustoMascotaEntity> gustosMascota = gustos.stream().map(gusto -> {
            GustoMascotaEntity gm = new GustoMascotaEntity();
            gm.setMasc_id(mascotaGuardada);
            gm.setGust_id(gusto);
            return gm;
        }).collect(Collectors.toList());

        gustoMascotaRepository.saveAll(gustosMascota);

        if (imagenUrls != null && !imagenUrls.isEmpty()) {
            List<ImagenEntity> imagenes = imagenUrls.stream().map(imagenUrl -> {
                ImagenEntity imagen = new ImagenEntity();
                imagen.setIma_url(imagenUrl);
                imagen.setMascota(mascotaGuardada); 
                return imagen;
            }).collect(Collectors.toList());

            imagenRepository.saveAll(imagenes);
        }

        return mascotaGuardada;
    }

    public List<MascotaImagenDTO> listarMascotasCards() {
        List<MascotaEntity> mascotas = mascotaRepository.findByMascEstado(1); 
        
        return mascotas.stream().map(mascota -> {          
            List<String> imagenUrls = mascota.getImagenes().stream()
                                            .map(ImagenEntity::getIma_url)
                                            .limit(2)  // Solo las primeras dos imágenes
                                            .collect(Collectors.toList());
            return new MascotaImagenDTO(mascota.getMasc_id(), mascota.getMasc_nombre(), imagenUrls);
        }).collect(Collectors.toList());
    }
    
    public List<MascotaEntity> ListarMascotasActivas() {
        return mascotaRepository.findByMascEstado(1);        
    }
   
    @Transactional
    public Optional<MascotaEntity> actualizarMascota(Long id, MascotaEntity mascotaActualizada, List<Long> nuevosGustosIds, List<String> nuevasImagenUrls) {
        Optional<MascotaEntity> mascotaOpt = mascotaRepository.findById(id);
        if (mascotaOpt.isPresent()) {
            MascotaEntity mascotaExistente = mascotaOpt.get();
            
            mascotaExistente.setMasc_nombre(mascotaActualizada.getMasc_nombre());
            mascotaExistente.setMasc_fecha_nacimiento(mascotaActualizada.getMasc_fecha_nacimiento());
            mascotaExistente.setMasc_historia(mascotaActualizada.getMasc_historia());
            mascotaExistente.setMasc_observacion(mascotaActualizada.getMasc_observacion());
            mascotaExistente.setMasc_estado(mascotaActualizada.getMasc_estado());
            mascotaExistente.setSexo(mascotaActualizada.getSexo());
            mascotaExistente.setTamanio(mascotaActualizada.getTamanio());
            mascotaExistente.setNivel_energia(mascotaActualizada.getNivel_energia());
            mascotaExistente.setTipo_mascota(mascotaActualizada.getTipo_mascota());
            mascotaExistente.setEstado_salud(mascotaActualizada.getEstado_salud());
            mascotaExistente.setEstado_vacuna(mascotaActualizada.getEstado_vacuna());
         
            MascotaEntity mascotaGuardada = mascotaRepository.save(mascotaExistente);          

            return Optional.of(mascotaGuardada);
        }
        return Optional.empty();
    }

    @Transactional
    public MascotaEntity cambiarEstadoMascota(Long evenId, Integer nuevoEstado) {
        Optional<MascotaEntity> mascotaExistente = mascotaRepository.findById(evenId);
        
        if (mascotaExistente.isPresent()) {
            MascotaEntity mascota = mascotaExistente.get();
            mascota.setMasc_estado(nuevoEstado);
            return mascotaRepository.save(mascota);
        } else {
            return null; // Si el evento no existe, retorna null
        }
    }

    // Filtros *****************************************************
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
    // *************************************************************

    // Filtros múltiples con parámetros opcionales
    public List<MascotaImagenDTO> filtrarPorFiltros(Long sexId, Long tamId, Long nienId, Long tipmaId) {
        List<MascotaEntity> mascotas = mascotaRepository.findByFilters(sexId,tamId,nienId,tipmaId);
        return mascotas.stream().map(mascota -> {
            List<String> imagenUrls = mascota.getImagenes().stream()
                    .map(ImagenEntity::getIma_url)
                    .limit(2)  // Solo las primeras dos imágenes
                    .collect(Collectors.toList());
            return new MascotaImagenDTO(mascota.getMasc_id(), mascota.getMasc_nombre(), imagenUrls);
        }).collect(Collectors.toList());
    }
    // *************************************************************
}
