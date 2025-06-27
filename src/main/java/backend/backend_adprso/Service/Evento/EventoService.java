package backend.backend_adprso.Service.Evento;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import backend.backend_adprso.Entity.Adopcion.AdopcionEntity;
import backend.backend_adprso.Service.Cloudinary.CloudinaryService;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.backend_adprso.Entity.Evento.EventoEntity;
import backend.backend_adprso.Entity.Evento.EventoUsuarioEntity;
import backend.backend_adprso.Entity.Usuario.UsuarioEntity;
import backend.backend_adprso.Repository.EventoRepository;
import backend.backend_adprso.Repository.EventoUsuarioRepository;
import backend.backend_adprso.Service.Usuario.UsuarioService;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


@Service
public class EventoService {
    @Autowired
    EventoRepository eventoRepository;

    @Autowired
    private EventoUsuarioRepository eventoUsuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CloudinaryService cloudinaryService;

    public List<EventoEntity> ListarEventos() {
        return eventoRepository.findAll();
    }

    public Optional<EventoEntity> ObtenerEventoPorId(Long id) {
        return eventoRepository.findById(id);
    }

    @Transactional
    public EventoEntity cambiarEstadoEvento(Long evenId, Integer nuevoEstado) {
        Optional<EventoEntity> eventoExistente = eventoRepository.findById(evenId);
        
        if (eventoExistente.isPresent()) {
            EventoEntity evento = eventoExistente.get();
            evento.setEven_estado(nuevoEstado);
            return eventoRepository.save(evento);
        } else {
            return null; // Si el evento no existe, retorna null
        }
    }

    public EventoEntity RegistrarEvento(EventoEntity evento) {
        evento.setEven_estado(1);
        return eventoRepository.save(evento);
    }

    public EventoEntity RegistrarEventoConImagen(EventoEntity evento, MultipartFile imagen) throws IOException {
        if (imagen != null && !imagen.isEmpty()) {
            Map<String, Object> resultado = cloudinaryService.subirImagen(imagen);
            evento.setEven_imagen((String) resultado.get("secure_url"));
            evento.setEven_imagen_public_id((String) resultado.get("public_id"));
        }

        evento.setEven_estado(1);
        return eventoRepository.save(evento);
    }

    public List<EventoEntity> ListarEventosActivos() {
        return eventoRepository.findByEvenEstado(1); 
    }
     
    public EventoEntity ActualizarEvento(Long id, EventoEntity eventoActualizado) {
        Optional<EventoEntity> eventoExistente = eventoRepository.findById(id);
        
        if (eventoExistente.isPresent()) {
            EventoEntity evento = eventoExistente.get();            
            
            if (eventoActualizado.getEven_nombre() != null) {
                evento.setEven_nombre(eventoActualizado.getEven_nombre());
            }
            if (eventoActualizado.getEven_descripcion() != null) {
                evento.setEven_descripcion(eventoActualizado.getEven_descripcion());
            }
            if (eventoActualizado.getEven_fecha_inicio() != null) {
                evento.setEven_fecha_inicio(eventoActualizado.getEven_fecha_inicio());
            }
            if (eventoActualizado.getEven_fecha_fin() != null) {
                evento.setEven_fecha_fin(eventoActualizado.getEven_fecha_fin());
            }
            if (eventoActualizado.getEven_lugar() != null) {
                evento.setEven_lugar(eventoActualizado.getEven_lugar());
            }
            if (eventoActualizado.getEven_imagen() != null) {
                evento.setEven_imagen(eventoActualizado.getEven_imagen());
            }
            if (eventoActualizado.getEven_estado() != null) {
                evento.setEven_estado(eventoActualizado.getEven_estado());
            }

            return eventoRepository.save(evento);
        } else {
            return null; 
        }
    }

    public EventoEntity ActualizarEventoConImagen(Long id, EventoEntity eventoActualizado, MultipartFile nuevaImagen) throws IOException {
        Optional<EventoEntity> eventoExistente = eventoRepository.findById(id);

        if (eventoExistente.isPresent()) {
            EventoEntity evento = eventoExistente.get();

            if (nuevaImagen != null && !nuevaImagen.isEmpty()) {
                // Eliminar imagen anterior
                if (evento.getEven_imagen_public_id() != null) {
                    cloudinaryService.eliminarImagen(evento.getEven_imagen_public_id());
                }
                // Subir nueva imagen
                Map<String, Object> resultado = cloudinaryService.subirImagen(nuevaImagen);
                evento.setEven_imagen((String) resultado.get("secure_url"));
                evento.setEven_imagen_public_id((String) resultado.get("public_id"));
            }

            // Actualizar otros campos
            if (eventoActualizado.getEven_nombre() != null) evento.setEven_nombre(eventoActualizado.getEven_nombre());
            if (eventoActualizado.getEven_descripcion() != null) evento.setEven_descripcion(eventoActualizado.getEven_descripcion());
            if (eventoActualizado.getEven_fecha_inicio() != null) evento.setEven_fecha_inicio(eventoActualizado.getEven_fecha_inicio());
            if (eventoActualizado.getEven_fecha_fin() != null) evento.setEven_fecha_fin(eventoActualizado.getEven_fecha_fin());
            if (eventoActualizado.getEven_lugar() != null) evento.setEven_lugar(eventoActualizado.getEven_lugar());
            if (eventoActualizado.getEven_estado() != null) evento.setEven_estado(eventoActualizado.getEven_estado());

            return eventoRepository.save(evento);
        } else {
            return null;
        }
    }

    public List<UsuarioEntity> obtenerUsuariosPorEvento(Long eventoId) {
        List<EventoUsuarioEntity> eventoUsuarioList = eventoUsuarioRepository.findByEventoEvenId(eventoId);
        return eventoUsuarioList.stream()
                .map(EventoUsuarioEntity::getUsuario) 
                .collect(Collectors.toList());
    }

    public List<EventoEntity> listarEventosInactivos() {
        return eventoRepository.findByEvenEstado(0); 
    }    

    public List<EventoEntity> buscarPorNombre(String nombre) {
        return eventoRepository.buscarPorNombre(nombre);
    }

    /*********************************************************************************************** */
    /* Eventos asociado al usuario logueado */

    public void guardarEventoUsuario(Long eventoId, String token) {
        UsuarioEntity usuarioLogueado = usuarioService.obtenerUsuarioLogueado(token);

        if (!eventoUsuarioRepository.existsByEventoAndUsuario(eventoId, usuarioLogueado.getUsr_id())) {
            EventoEntity evento = eventoRepository.findById(eventoId)
                    .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

            EventoUsuarioEntity eventoUsuario = new EventoUsuarioEntity();
            eventoUsuario.setEvento(evento);
            eventoUsuario.setUsuario(usuarioLogueado);

            eventoUsuarioRepository.save(eventoUsuario);
        } else {
            throw new RuntimeException("El usuario ya está registrado para este evento");
        }
    }

    public List<EventoEntity> listarEventosDelUsuario(String token) {
        UsuarioEntity usuarioLogueado = usuarioService.obtenerUsuarioLogueado(token);
        List<EventoUsuarioEntity> eventoUsuarios = eventoUsuarioRepository.findByUsuario(usuarioLogueado);

        List<EventoEntity> eventos = new ArrayList<>();
        for (EventoUsuarioEntity eventoUsuario : eventoUsuarios) {
            eventos.add(eventoUsuario.getEvento());
        }
        return eventos;
    }

    public byte[] generarReporteInscripciones(List<Long> idsFiltrados) throws IOException {
        List<EventoEntity> eventos = (idsFiltrados == null || idsFiltrados.isEmpty())
                ? eventoRepository.findAll()
                : eventoRepository.findAllById(idsFiltrados);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Inscripciones");

            // Estilos
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);

            CellStyle boldStyle = workbook.createCellStyle();
            boldStyle.setFont(boldFont);

            // Usa XSSFCellStyle
            XSSFCellStyle headerStyle = (XSSFCellStyle) workbook.createCellStyle();
            headerStyle.setFont(boldFont);

            // Define el color personalizado (color hexadecimal #dda15e)
            byte[] rgb = new byte[]{(byte) 221, (byte) 161, (byte) 94}; // RGB como bytes
            XSSFColor colorPersonalizado = new XSSFColor(rgb, null);
            headerStyle.setFillForegroundColor(colorPersonalizado);
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Bordes
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // Borde para celdas normales
            CellStyle borderedStyle = workbook.createCellStyle();
            borderedStyle.setBorderBottom(BorderStyle.THIN);
            borderedStyle.setBorderTop(BorderStyle.THIN);
            borderedStyle.setBorderLeft(BorderStyle.THIN);
            borderedStyle.setBorderRight(BorderStyle.THIN);

            int rowIdx = 0;

            for (EventoEntity evento : eventos) {
                List<UsuarioEntity> inscritos = eventoUsuarioRepository.findUsuariosByEventoId(evento.getEven_id());

                // Título del evento
                Row titulo = sheet.createRow(rowIdx++);
                Cell celdaTitulo = titulo.createCell(0);
                celdaTitulo.setCellValue("Evento: " + evento.getEven_nombre());
                celdaTitulo.setCellStyle(boldStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowIdx - 1, rowIdx - 1, 0, 4));
                // Detalles del evento
                rowIdx = agregarDetalle(sheet, rowIdx, "Fecha de Inicio", evento.getEven_fecha_inicio() != null ? evento.getEven_fecha_inicio().toString() : "", boldStyle);
                rowIdx = agregarDetalle(sheet, rowIdx, "Fecha de Fin", evento.getEven_fecha_fin() != null ? evento.getEven_fecha_fin().toString() : "", boldStyle);
                rowIdx = agregarDetalle(sheet, rowIdx, "Lugar", evento.getEven_lugar(), boldStyle);
                rowIdx = agregarDetalle(sheet, rowIdx, "Estado", evento.getEven_estado() != null && evento.getEven_estado() == 1 ? "Activo" : "Inactivo", boldStyle);
                Row participantesTitulo = sheet.createRow(rowIdx++);
                Cell participantesCelda = participantesTitulo.createCell(0);
                participantesCelda.setCellValue("Lista de participantes");
                CellStyle centerBoldStyle = workbook.createCellStyle();
                Font centerBoldFont = workbook.createFont();
                centerBoldFont.setBold(true);
                centerBoldStyle.setFont(centerBoldFont);
                centerBoldStyle.setAlignment(HorizontalAlignment.CENTER);
                participantesCelda.setCellStyle(centerBoldStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowIdx - 1, rowIdx - 1, 0, 4));

                // Encabezado de participantes
                Row header = sheet.createRow(rowIdx++);
                String[] columnas = { "Nombre", "Apellido", "Documento", "Email", "Teléfono" };
                for (int i = 0; i < columnas.length; i++) {
                    Cell celda = header.createCell(i);
                    celda.setCellValue(columnas[i]);
                    celda.setCellStyle(headerStyle);
                }

                // Participantes
                for (UsuarioEntity usuario : inscritos) {
                    Row row = sheet.createRow(rowIdx++);
                    row.createCell(0).setCellValue(usuario.getUsr_nombre());
                    row.createCell(1).setCellValue(usuario.getUsr_apellido());
                    row.createCell(2).setCellValue(usuario.getUsr_documento());
                    row.createCell(3).setCellValue(usuario.getUsr_email());
                    row.createCell(4).setCellValue(usuario.getUsr_telefono());

                    for (int i = 0; i < 5; i++) {
                        row.getCell(i).setCellStyle(borderedStyle);
                    }
                }

                // Total
                Row totalRow = sheet.createRow(rowIdx++);
                Cell totalLabel = totalRow.createCell(0);
                totalLabel.setCellValue("Total inscritos:");
                totalLabel.setCellStyle(boldStyle);
                totalRow.createCell(1).setCellValue(inscritos.size());

                rowIdx += 2; // espacio entre eventos
            }

            // Ajustar ancho de columnas
            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    // Método auxiliar para agregar detalles con estilo
    private int agregarDetalle(Sheet sheet, int rowIdx, String label, String value, CellStyle labelStyle) {
        Row row = sheet.createRow(rowIdx++);
        Cell labelCell = row.createCell(0);
        labelCell.setCellValue(label + ":");
        labelCell.setCellStyle(labelStyle);
        row.createCell(1).setCellValue(value);
        return rowIdx;
    }



}
