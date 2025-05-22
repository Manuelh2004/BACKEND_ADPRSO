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

}
