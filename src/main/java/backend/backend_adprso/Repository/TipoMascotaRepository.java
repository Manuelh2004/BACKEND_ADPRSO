package backend.backend_adprso.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.backend_adprso.Entity.Items.TipoMascotaEntity;

@Repository
public interface TipoMascotaRepository extends JpaRepository<TipoMascotaEntity, Long> {
    
}
