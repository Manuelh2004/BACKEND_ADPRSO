package backend.backend_adprso.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.backend_adprso.Entity.Items.TipoPlanEntity;

@Repository
public interface TipoPlanRepository extends JpaRepository<TipoPlanEntity, Long> {
    
}
