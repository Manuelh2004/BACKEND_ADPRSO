package backend.backend_adprso.Service.Mascota;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.*;

import backend.backend_adprso.Entity.Mascota.MascotaDetalleDTO;
import backend.backend_adprso.Service.Cloudinary.CloudinaryService;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.web.multipart.MultipartFile;

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
    private CloudinaryService cloudinaryService;

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
                .map(ImagenEntity::getImaUrl)  // asumiendo campo imag_url
                .toList();
        dto.setImagenes_url(urls);

        return dto;
    }
    
    @Transactional
    public MascotaEntity RegistrarMascota(MascotaEntity mascota, List<Long> gustosIds, List<MultipartFile> imagenes) {
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

        if (imagenes != null && !imagenes.isEmpty()) {
            for (MultipartFile imagen : imagenes) {
                try {
                    Map<String, Object> resultado = cloudinaryService.subirImagen(imagen);
                    ImagenEntity img = new ImagenEntity();
                    img.setImaUrl((String) resultado.get("secure_url"));
                    img.setImaPublicId((String) resultado.get("public_id"));
                    img.setMascota(mascotaGuardada);
                    imagenRepository.save(img);
                } catch (IOException e) {
                    e.printStackTrace(); // o usa logger
                }
            }
        }

        return mascotaGuardada;
    }

    public List<MascotaImagenDTO> listarMascotasCards() {
        List<MascotaEntity> mascotas = mascotaRepository.findByMascEstado(1); 
        
        return mascotas.stream().map(mascota -> {          
            List<String> imagenUrls = mascota.getImagenes().stream()
                                            .map(ImagenEntity::getImaUrl)
                                            .limit(2)  // Solo las primeras dos imágenes
                                            .collect(Collectors.toList());
            return new MascotaImagenDTO(mascota.getMasc_id(), mascota.getMasc_nombre(), imagenUrls);
        }).collect(Collectors.toList());
    }
    
    public List<MascotaEntity> ListarMascotasActivas() {
        return mascotaRepository.findByMascEstado(1);        
    }

    @Transactional
    public MascotaEntity updateMascota(Long id, MascotaEntity mascotaDetails, List<MultipartFile> nuevasImagenes, List<Long> updatedGustos, List<String> imagenesAEliminar) {
        Optional<MascotaEntity> existingMascotaOpt = mascotaRepository.findById(id);

        if (existingMascotaOpt.isEmpty()) {
            throw new RuntimeException("Mascota no encontrada con el ID: " + id);
        }

        MascotaEntity existingMascota = existingMascotaOpt.get();

        // Actualizar datos generales
        existingMascota.setMasc_nombre(mascotaDetails.getMasc_nombre());
        existingMascota.setMasc_fecha_nacimiento(mascotaDetails.getMasc_fecha_nacimiento());
        existingMascota.setMasc_historia(mascotaDetails.getMasc_historia());
        existingMascota.setMasc_observacion(mascotaDetails.getMasc_observacion());
        existingMascota.setSexo(mascotaDetails.getSexo());
        existingMascota.setTamanio(mascotaDetails.getTamanio());
        existingMascota.setNivel_energia(mascotaDetails.getNivel_energia());
        existingMascota.setTipo_mascota(mascotaDetails.getTipo_mascota());
        existingMascota.setEstado_salud(mascotaDetails.getEstado_salud());
        existingMascota.setEstado_vacuna(mascotaDetails.getEstado_vacuna());

        MascotaEntity updatedMascota = mascotaRepository.save(existingMascota);

        // ✅ Eliminar solo las imágenes que el usuario marcó para borrar
        if (imagenesAEliminar != null && !imagenesAEliminar.isEmpty()) {
            for (String publicId : imagenesAEliminar) {
                ImagenEntity img = imagenRepository.findByImaPublicId(publicId);
                System.out.println("Eliminando imagen con publicId: " + publicId);
                if (img != null) {
                    try {
                        cloudinaryService.eliminarImagen(publicId);
                        imagenRepository.delete(img);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // ✅ Agregar nuevas imágenes
        if (nuevasImagenes != null) {
            for (MultipartFile nuevaImagen : nuevasImagenes) {
                try {
                    Map<String, Object> resultado = cloudinaryService.subirImagen(nuevaImagen);
                    ImagenEntity imagen = new ImagenEntity();
                    imagen.setMascota(updatedMascota);
                    imagen.setImaUrl((String) resultado.get("secure_url"));
                    imagen.setImaPublicId((String) resultado.get("public_id"));
                    imagenRepository.save(imagen);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // ✅ Actualizar gustos
        gustoMascotaRepository.deleteByMascotaId(id);
        if (updatedGustos != null) {
            for (Long gustoId : updatedGustos) {
                GustoEntity gustoEntity = gustoRepository.findByGust_id(gustoId);
                if (gustoEntity != null) {
                    GustoMascotaEntity gustoMascota = new GustoMascotaEntity();
                    gustoMascota.setMasc_id(updatedMascota);
                    gustoMascota.setGust_id(gustoEntity);
                    gustoMascotaRepository.save(gustoMascota);
                }
            }
        }

        return updatedMascota;
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
    public void generarExcelMascotas(ByteArrayOutputStream outputStream) throws IOException {
        List<MascotaEntity> mascotas = mascotaRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Mascotas");

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);

        XSSFColor headerColor = new XSSFColor(new java.awt.Color(221, 161, 94), null); // Color #dda15e
        headerStyle.setFillForegroundColor(headerColor);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle normalStyle = workbook.createCellStyle();
        normalStyle.setBorderBottom(BorderStyle.THIN);
        normalStyle.setBorderTop(BorderStyle.THIN);
        normalStyle.setBorderLeft(BorderStyle.THIN);
        normalStyle.setBorderRight(BorderStyle.THIN);
        normalStyle.setAlignment(HorizontalAlignment.CENTER);
        normalStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        Row headerRow = sheet.createRow(0);
        String[] headers = {"#", "Nombre", "Edad", "Especie", "Sexo", "Gusto"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle); 
        }

        int rowNum = 1;
        for (MascotaEntity mascota : mascotas) {
            Row row = sheet.createRow(rowNum++);

            Cell cell0 = row.createCell(0);
            cell0.setCellValue(rowNum - 1); 
            cell0.setCellStyle(normalStyle);

            String nombre = (mascota.getMasc_nombre() != null) ? mascota.getMasc_nombre() : "Sin valores";
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(nombre);
            cell1.setCellStyle(normalStyle);

            String fechaNacimiento = (mascota.getMasc_fecha_nacimiento() != null) ? mascota.getMasc_fecha_nacimiento().toString() : "Sin valores";
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(fechaNacimiento); 
            cell2.setCellStyle(normalStyle);

            String especie = (mascota.getTipo_mascota() != null && mascota.getTipo_mascota().getTipma_nombre() != null)
                    ? mascota.getTipo_mascota().getTipma_nombre() : "Sin valores";
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(especie);
            cell3.setCellStyle(normalStyle);

            String sexo = (mascota.getSexo() != null && mascota.getSexo().getSex_nombre() != null)
                    ? mascota.getSexo().getSex_nombre() : "Sin valores";
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(sexo);
            cell4.setCellStyle(normalStyle);

            String gustos = "Sin valores";
            if (mascota.getGustoMascotaList() != null && !mascota.getGustoMascotaList().isEmpty()) {
                StringBuilder gustoBuilder = new StringBuilder();
                for (GustoMascotaEntity gustoMascota : mascota.getGustoMascotaList()) {
                    gustoBuilder.append(gustoMascota.getGust_id().getGust_nombre()).append(", ");
                }
                gustos = gustoBuilder.toString().replaceAll(", $", "");
            }
            Cell cell5 = row.createCell(5);
            cell5.setCellValue(gustos);
            cell5.setCellStyle(normalStyle);
        }
        if (mascotas.isEmpty()) {
            Row row = sheet.createRow(1);  
            for (int i = 0; i < headers.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellStyle(normalStyle);
            }
        }
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);  
        }
        workbook.write(outputStream);
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
                    .map(ImagenEntity::getImaUrl)
                    .limit(2)  // Solo las primeras dos imágenes
                    .collect(Collectors.toList());
            return new MascotaImagenDTO(mascota.getMasc_id(), mascota.getMasc_nombre(), imagenUrls);
        }).collect(Collectors.toList());
    }
    // *************************************************************
}
