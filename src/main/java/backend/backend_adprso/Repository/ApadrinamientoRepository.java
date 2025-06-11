package backend.backend_adprso.Repository;


import backend.backend_adprso.Entity.Planes.ApadrinamientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApadrinamientoRepository extends JpaRepository<ApadrinamientoEntity, Long>{
    
}
