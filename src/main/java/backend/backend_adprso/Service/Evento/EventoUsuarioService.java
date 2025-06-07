package backend.backend_adprso.Service.Evento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import backend.backend_adprso.Entity.Evento.EventoUsuarioEntity;
import backend.backend_adprso.Entity.Evento.EventoEntity;
import backend.backend_adprso.Entity.Usuario.UsuarioEntity;
import backend.backend_adprso.Repository.EventoUsuarioRepository;
import backend.backend_adprso.Repository.EventoRepository;
import backend.backend_adprso.Repository.UsuarioRepository;

@Service
public class EventoUsuarioService {

    @Autowired
    private EventoUsuarioRepository eventoUsuarioRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

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