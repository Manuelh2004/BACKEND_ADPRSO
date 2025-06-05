package backend.backend_adprso.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import backend.backend_adprso.Entity.Items.TipoUsuarioEntity;

public interface TipoUsuarioRepository extends JpaRepository <TipoUsuarioEntity, Long>{
    @Query("SELECT t FROM TipoUsuarioEntity t WHERE t.tipus_nombre = :tipus_nombre")
    Optional<TipoUsuarioEntity> findByTipusNombre(@Param("tipus_nombre") String tipus_nombre);

}
