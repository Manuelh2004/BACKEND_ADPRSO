package backend.backend_adprso.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.backend_adprso.Entity.Items.EstadoSaludEntity;

@Repository
public interface EstadoSaludRepository extends JpaRepository<EstadoSaludEntity, Long>{
    
}
