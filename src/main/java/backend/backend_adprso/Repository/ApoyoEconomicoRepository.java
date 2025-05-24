package backend.backend_adprso.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.backend_adprso.Entity.Planes.ApoyoEconomicoEntity;

@Repository
public interface ApoyoEconomicoRepository extends JpaRepository<ApoyoEconomicoEntity, Long>{
    
}
