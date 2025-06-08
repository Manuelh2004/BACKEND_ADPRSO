package backend.backend_adprso.Service.Evento;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.backend_adprso.Entity.Evento.EventoEntity;
import backend.backend_adprso.Entity.Evento.EventoUsuarioEntity;
import backend.backend_adprso.Entity.Usuario.UsuarioEntity;
import backend.backend_adprso.Repository.EventoRepository;
import backend.backend_adprso.Repository.EventoUsuarioRepository;
import backend.backend_adprso.Repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
public class EventoService {
    @Autowired
    EventoRepository eventoRepository;
    @Autowired
    private EventoUsuarioRepository eventoUsuarioRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

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

    /*********************************************************************************************** */

    // Método para registrar un evento_usuario asignando un evento a un usuario
    public EventoUsuarioEntity registrarEventoUsuario(Long eventoId, Long usuarioId) {
        // Buscar el evento por su ID
        EventoEntity evento = eventoRepository.findById(eventoId).orElse(null);

        // Buscar el usuario por su ID
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId).orElse(null);

        // Verificar si tanto el evento como el usuario existen
        if (evento == null || usuario == null) {
            throw new IllegalArgumentException("Evento o Usuario no encontrado.");
        }

        // Crear la entidad EventoUsuarioEntity y asignar el evento y el usuario
        EventoUsuarioEntity eventoUsuario = new EventoUsuarioEntity();
        eventoUsuario.setEvento(evento);
        eventoUsuario.setUsuario(usuario);

        // Guardar la relación en la base de datos
        return eventoUsuarioRepository.save(eventoUsuario);
    }

}
