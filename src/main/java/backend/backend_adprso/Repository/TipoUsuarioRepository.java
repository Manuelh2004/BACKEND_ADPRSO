package backend.backend_adprso.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import backend.backend_adprso.Entity.Items.TipoUsuarioEntity;

public interface TipoUsuarioRepository extends JpaRepository <TipoUsuarioEntity, Long>{
    @Query("SELECT t FROM TipoUsuarioEntity t WHERE t.tipus_nombre = :tipus_nombre")
    boolean existsByUsrEmail(String email);

}
