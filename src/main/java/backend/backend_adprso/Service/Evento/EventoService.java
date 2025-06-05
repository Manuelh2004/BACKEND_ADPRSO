package backend.backend_adprso.Service.Evento;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.backend_adprso.Entity.Evento.EventoEntity;
import backend.backend_adprso.Repository.EventoRepository;

@Service
public class EventoService {
    @Autowired
    EventoRepository eventoRepository;

    public List<EventoEntity> ListarEventos() {
        return eventoRepository.findAll();
    }

    public Optional<EventoEntity> ObtenerEventoPorId(Long id) {
        return eventoRepository.findById(id);
    }

    public EventoEntity RegistrarEvento(EventoEntity evento) {
        evento.setEven_estado(1);
        return eventoRepository.save(evento);
    }

    public void EliminarEvento(Long id) {
        eventoRepository.deleteById(id);
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

}
