package backend.backend_adprso.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.backend_adprso.Entity.Adopcion.AdopcionEntity;

@Repository
public interface AdopcionRepository extends JpaRepository<AdopcionEntity, Long>{
    
}
