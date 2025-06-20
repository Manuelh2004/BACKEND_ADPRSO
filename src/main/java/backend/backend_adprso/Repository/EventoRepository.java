package backend.backend_adprso.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.backend_adprso.Entity.Evento.EventoEntity;

@Repository
public interface EventoRepository extends JpaRepository<EventoEntity, Long>{
    @Query(value = "SELECT * FROM evento e WHERE e.even_estado = :estado", nativeQuery = true)
    List<EventoEntity> findByEvenEstado(@Param("estado") Integer estado);

    @Query("SELECT e FROM EventoEntity e WHERE LOWER(e.even_nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<EventoEntity> buscarPorNombre(String nombre);
}
