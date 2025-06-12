package backend.backend_adprso.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import backend.backend_adprso.Entity.Evento.EventoUsuarioEntity;

@Repository
public interface EventoUsuarioRepository extends JpaRepository<EventoUsuarioEntity, Long>{
    @Query("SELECT CASE WHEN COUNT(eu) > 0 THEN true ELSE false END " +
           "FROM EventoUsuarioEntity eu " +
           "WHERE eu.evento.even_id = :eventoId AND eu.usuario.usr_id = :usuarioId")
    boolean existsByEventoAndUsuario(Long eventoId, Long usuarioId);
}
