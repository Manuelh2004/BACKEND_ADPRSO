package backend.backend_adprso.Service.Mascota;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import backend.backend_adprso.Entity.Mascota.*;
import backend.backend_adprso.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.backend_adprso.Entity.Items.GustoEntity;
import backend.backend_adprso.Entity.Items.ImagenEntity;
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

    @Autowired
    SexoRepository sexoRepository;
    @Autowired
    TamanioRepository tamanioRepository;
    @Autowired
    NivelEnergiaRepository nivelEnergiaRepository;
    @Autowired
    TipoMascotaRepository tipoMascotaRepository;
    @Autowired
    EstadoSaludRepository estadoSaludRepository;
    @Autowired
    EstadoVacunaRepository estadoVacunaRepository;

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
    public Optional<MascotaDetalleDTO> obtenerDetalleMascota(Long id) {
        return mascotaRepository.findById(id)
                .map(this::mapToDetalleDto);
    }

    /* ---------- Mapper entity → DTO ---------- */
    private MascotaDetalleDTO mapToDetalleDto(MascotaEntity e) {

        MascotaDetalleDTO dto = new MascotaDetalleDTO();

        /* campos directos ------------------------------------ */
        dto.setMasc_id(e.getMasc_id());
        dto.setMasc_nombre(e.getMasc_nombre());
        dto.setMasc_fecha_nacimiento(e.getMasc_fecha_nacimiento());
        dto.setMasc_fecha_registro(e.getMasc_fecha_registro());
        dto.setMasc_historia(e.getMasc_historia());
        dto.setMasc_observacion(e.getMasc_observacion());
        dto.setMasc_estado(
                e.getMasc_estado() != null && e.getMasc_estado() == 1 ? "ACTIVO" : "INACTIVO"
        );

        /* catálogos (ManyToOne) ------------------------------ */
        dto.setSexo(          e.getSexo()          != null ? e.getSexo().getSex_nombre()          : null);
        dto.setTamanio(       e.getTamanio()       != null ? e.getTamanio().getTam_nombre()       : null);
        dto.setNivel_energia( e.getNivel_energia() != null ? e.getNivel_energia().getNien_nombre(): null);
        dto.setTipo_mascota(  e.getTipo_mascota()  != null ? e.getTipo_mascota().getTipma_nombre(): null);
        dto.setEstado_salud(  e.getEstado_salud()  != null ? e.getEstado_salud().getEstsa_nombre(): null);
        dto.setEstado_vacuna( e.getEstado_vacuna() != null ? e.getEstado_vacuna().getEstva_nombre(): null);

        /* gustos (OneToMany) -------------------------------- */
        List<String> gustos = e.getGustoMascotaList() == null
                ? List.of()
                : e.getGustoMascotaList()
                .stream()
                .map(gm -> gm.getGust_id().getGust_nombre())
                .toList();
        dto.setMasc_gustos(gustos);

        /* imágenes ------------------------------------------ */
        List<String> urls = e.getImagenes() == null
                ? List.of()
                : e.getImagenes()
                .stream()
                .map(ImagenEntity::getIma_url)  // asumiendo campo imag_url
                .toList();
        dto.setImagenes_url(urls);

        return dto;
    }

    @Transactional
    public MascotaEntity registrar(MascotaRequestDTO dto) {
        MascotaEntity mascota = new MascotaEntity();

        mascota.setMasc_nombre(dto.getNombre());
        mascota.setMasc_fecha_nacimiento(dto.getFechaNacimiento());
        mascota.setMasc_fecha_registro(LocalDate.now());
        mascota.setMasc_historia(dto.getHistoria());
        mascota.setMasc_observacion(dto.getObservacion());
        mascota.setMasc_estado(1); // Activa

        // Relaciones
        mascota.setSexo(sexoRepository.findById(dto.getSexoId()).orElseThrow());
        mascota.setTamanio(tamanioRepository.findById(dto.getTamanioId()).orElseThrow());
        mascota.setNivel_energia(nivelEnergiaRepository.findById(dto.getNivelEnergiaId()).orElseThrow());
        mascota.setTipo_mascota(tipoMascotaRepository.findById(dto.getTipoMascotaId()).orElseThrow());
        mascota.setEstado_salud(estadoSaludRepository.findById(dto.getEstadoSaludId()).orElseThrow());
        mascota.setEstado_vacuna(estadoVacunaRepository.findById(dto.getEstadoVacunaId()).orElseThrow());

        // Guardamos mascota primero (para tener ID)
        MascotaEntity mascotaGuardada = mascotaRepository.save(mascota);

        // Guardar imágenes
        if (dto.getImagenesUrls() != null) {
            for (String url : dto.getImagenesUrls()) {
                ImagenEntity imagen = new ImagenEntity();
                imagen.setIma_url(url);
                imagen.setMascota(mascotaGuardada);
                imagenRepository.save(imagen);
            }
        }

        // Guardar gustos
        if (dto.getGustoIds() != null) {
            for (Long gustoId : dto.getGustoIds()) {
                GustoEntity gusto = gustoRepository.findById(gustoId).orElseThrow();
                GustoMascotaEntity gm = new GustoMascotaEntity();
                gm.setMasc_id(mascotaGuardada);
                gm.setGust_id(gusto);
                gustoMascotaRepository.save(gm);
            }
        }

        return mascotaGuardada;
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
