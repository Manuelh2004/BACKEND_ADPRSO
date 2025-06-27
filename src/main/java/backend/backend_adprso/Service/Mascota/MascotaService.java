package backend.backend_adprso.Service.Mascota;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.*;

import backend.backend_adprso.Entity.Mascota.MascotaDetalleDTO;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.backend_adprso.Entity.Evento.EventoEntity;
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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

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
            mascota.getGustoNames();  
            List<ImagenEntity> imagenes = imagenRepository.findByMascotaId(id); 
            mascota.setImagenes(imagenes); 
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
    public MascotaEntity updateMascota(Long id, MascotaEntity mascotaDetails, List<String> updatedImagenes, List<Long> updatedGustos) {
        // Buscar la mascota por su ID
        Optional<MascotaEntity> existingMascotaOpt = mascotaRepository.findById(id);

        if (existingMascotaOpt.isPresent()) {
            MascotaEntity existingMascota = existingMascotaOpt.get();

            // Actualizamos los campos de la mascota
            existingMascota.setMasc_nombre(mascotaDetails.getMasc_nombre());
            existingMascota.setMasc_fecha_nacimiento(mascotaDetails.getMasc_fecha_nacimiento());
            existingMascota.setMasc_historia(mascotaDetails.getMasc_historia());
            existingMascota.setMasc_observacion(mascotaDetails.getMasc_observacion());

            // Actualizamos las relaciones
            existingMascota.setSexo(mascotaDetails.getSexo());
            existingMascota.setTamanio(mascotaDetails.getTamanio());
            existingMascota.setNivel_energia(mascotaDetails.getNivel_energia());
            existingMascota.setTipo_mascota(mascotaDetails.getTipo_mascota());
            existingMascota.setEstado_salud(mascotaDetails.getEstado_salud());
            existingMascota.setEstado_vacuna(mascotaDetails.getEstado_vacuna());

            // Guardamos la mascota actualizada
            MascotaEntity updatedMascota = mascotaRepository.save(existingMascota);

            // 1. Eliminar las imágenes existentes de la mascota antes de agregar las nuevas
            if (updatedImagenes != null && !updatedImagenes.isEmpty()) {
                System.out.println("Eliminando imágenes asociadas a la mascota con ID: " + id);
                imagenRepository.deleteByMascotaId(id);  // Elimina todas las imágenes asociadas a la mascota
                // Añadimos las nuevas imágenes
                for (String imagenUrl : updatedImagenes) {
                    ImagenEntity imagen = new ImagenEntity();
                    imagen.setMascota(updatedMascota);  // Asociamos la imagen con la mascota actualizada
                    imagen.setIma_url(imagenUrl);  // Usamos la URL proporcionada
                    imagenRepository.save(imagen);
                }
            }

            // 2. Eliminar los gustos existentes de la mascota antes de agregar los nuevos
            if (updatedGustos != null && !updatedGustos.isEmpty()) {
                gustoMascotaRepository.deleteByMascotaId(id);  // Elimina todos los gustos asociados a la mascota
                // Añadimos los nuevos gustos
                for (Long gustoId : updatedGustos) {
                    GustoEntity gustoEntity = gustoRepository.findByGust_id(gustoId);  // Obtenemos el GustoEntity por ID
                    if (gustoEntity != null) {
                        GustoMascotaEntity gustoMascota = new GustoMascotaEntity();
                        gustoMascota.setMasc_id(updatedMascota);  // Asociamos el gusto con la mascota actualizada
                        gustoMascota.setGust_id(gustoEntity);  // Asignamos el gusto encontrado
                        gustoMascotaRepository.save(gustoMascota);
                    }
                }
            }

            return updatedMascota;
        } else {
            // Si no existe una mascota con ese ID, lanzar excepción o devolver null
            throw new RuntimeException("Mascota no encontrada con el ID: " + id);
        }
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

    @Transactional()
    public List<MascotaEntity> buscarPorNombre(String nombre) {
        return mascotaRepository.buscarPorNombre(nombre);
    }

    public List<MascotaEntity> listarMascotasInactivas() {
        return mascotaRepository.findByMascEstado(0); 
    } 

    @Transactional
    public void generarExcelMascotas() throws IOException {
        List<MascotaEntity> mascotas = mascotaRepository.findAll();

        // Crear un libro de trabajo de Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Mascotas");

        // Crear la fila de encabezado
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Nombre", "Edad", "Especie", "Raza", "Gusto"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Llenar las filas con los datos de las mascotas
        int rowNum = 1;
        for (MascotaEntity mascota : mascotas) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(mascota.getMasc_id());
            row.createCell(1).setCellValue(mascota.getMasc_nombre());
            row.createCell(2).setCellValue(mascota.getMasc_fecha_nacimiento());
            row.createCell(3).setCellValue(mascota.getTipo_mascota().getTipma_nombre());
            row.createCell(4).setCellValue(mascota.getSexo().getSex_nombre());

            // Si la entidad GustoEntity está relacionada, agregar el gusto de la mascota
            if (mascota.getGustoMascotaList() != null && !mascota.getGustoMascotaList().isEmpty()) {
                StringBuilder gustos = new StringBuilder();
                for (GustoMascotaEntity gustoMascota : mascota.getGustoMascotaList()) {
                    gustos.append(gustoMascota.getGust_id().getGust_nombre()).append(", ");
                }
                row.createCell(5).setCellValue(gustos.toString().replaceAll(", $", ""));
            }
        }

        // Escribir el archivo Excel
        try (FileOutputStream fileOut = new FileOutputStream("mascotas.xlsx")) {
            workbook.write(fileOut);
        }

        // Cerrar el workbook
        workbook.close();
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
