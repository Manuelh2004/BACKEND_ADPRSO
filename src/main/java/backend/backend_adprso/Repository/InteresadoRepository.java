package backend.backend_adprso.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import backend.backend_adprso.Entity.Usuario.InteresadoEntity;

@Repository
public interface InteresadoRepository extends JpaRepository<InteresadoEntity, Long> {

    
}

