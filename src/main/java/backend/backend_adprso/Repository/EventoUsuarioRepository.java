package backend.backend_adprso.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.backend_adprso.Entity.Evento.EventoEntity;
import backend.backend_adprso.Entity.Evento.EventoUsuarioEntity;
import backend.backend_adprso.Entity.Usuario.UsuarioEntity;

@Repository
public interface EventoUsuarioRepository extends JpaRepository<EventoUsuarioEntity, Long>{
    @Query("SELECT CASE WHEN COUNT(eu) > 0 THEN true ELSE false END " +
           "FROM EventoUsuarioEntity eu " +
           "WHERE eu.evento.even_id = :eventoId AND eu.usuario.usr_id = :usuarioId")
    boolean existsByEventoAndUsuario(Long eventoId, Long usuarioId);

    @Query("SELECT eu FROM EventoUsuarioEntity eu WHERE eu.usuario = :usuario")
    List<EventoUsuarioEntity> findByUsuario(@Param("usuario") UsuarioEntity usuario);

    @Query("SELECT eu FROM EventoUsuarioEntity eu WHERE eu.evento.even_id = :eventoId")
    List<EventoUsuarioEntity> findByEventoEvenId(@Param("eventoId") Long eventoId);

     @Query("SELECT e FROM EventoEntity e WHERE e.even_estado = :estado")
    List<EventoEntity> findEventosByEstado(@Param("estado") Integer estado);
    @Query("SELECT eu.usuario FROM EventoUsuarioEntity eu WHERE eu.evento.even_id = :eventoId")
    List<UsuarioEntity> findUsuariosByEventoId(@Param("eventoId") Long eventoId);
}
