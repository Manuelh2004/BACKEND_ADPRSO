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
        return mascotaRepository.findAllWithGustos();       
       
    }

    @Transactional
    public Optional<MascotaEntity> ObtenerMascotaPorId(Long id) {
        Optional<MascotaEntity> mascotaOpt = mascotaRepository.findById(id);
        if (mascotaOpt.isPresent()) {
            // Asegúrate de que los gustos se carguen correctamente
            MascotaEntity mascota = mascotaOpt.get();
            // Aquí se asegura que los gustos estén inicializados
            mascota.getGustoNames(); // Forza la carga de los gustos asociados a la mascota
            return Optional.of(mascota);
        }
        return Optional.empty();
    }
    
    @Transactional
    public MascotaEntity RegistrarMascota(MascotaEntity mascota, List<Long> gustosIds, List<String> imagenUrls) {
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


        // Guardar las imágenes relacionadas con la mascota
        if (imagenUrls != null && !imagenUrls.isEmpty()) {
            List<ImagenEntity> imagenes = imagenUrls.stream().map(imagenUrl -> {
                ImagenEntity imagen = new ImagenEntity();
                imagen.setIma_url(imagenUrl);
                imagen.setMascota(mascotaGuardada); // Relacionamos con la mascota
                return imagen;
            }).collect(Collectors.toList());

            // Guardamos las imágenes en la base de datos
            imagenRepository.saveAll(imagenes);
        }

        return mascotaGuardada;
    }
    

   @Transactional
    public void EliminarMascota(Long id) {
        // Primero, eliminamos las relaciones en la tabla gusto_mascota
        List<GustoMascotaEntity> gustoMascotas = gustoMascotaRepository.findByMascotaId(id);
        gustoMascotaRepository.deleteAll(gustoMascotas); // Eliminar todas las relaciones gusto-mascota asociadas

        // Ahora eliminamos la mascota de la tabla mascota
        mascotaRepository.deleteById(id);
    }

    
    public List<MascotaEntity> ListarMascotasActivas() {
        return mascotaRepository.findByMascEstado(1);        
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

    // Método para filtrar con múltiples parámetros opcionales
    public List<MascotaEntity> filtrarPorFiltros(Long sexId, Long tamId, Long nienId, Long tipmaId) {
        return mascotaRepository.findByFilters(sexId, tamId, nienId, tipmaId);
    }
    // *************************************************************
}
