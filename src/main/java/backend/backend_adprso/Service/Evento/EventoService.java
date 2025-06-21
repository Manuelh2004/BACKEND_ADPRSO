package backend.backend_adprso.Service.Evento;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.backend_adprso.Entity.Evento.EventoEntity;
import backend.backend_adprso.Entity.Evento.EventoUsuarioEntity;
import backend.backend_adprso.Entity.Usuario.UsuarioEntity;
import backend.backend_adprso.Repository.EventoRepository;
import backend.backend_adprso.Repository.EventoUsuarioRepository;
import backend.backend_adprso.Service.Usuario.UsuarioService;
import jakarta.transaction.Transactional;

@Service
public class EventoService {
    @Autowired
    EventoRepository eventoRepository;
    @Autowired
    private EventoUsuarioRepository eventoUsuarioRepository;
    @Autowired
    private UsuarioService usuarioService;

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


   

}
