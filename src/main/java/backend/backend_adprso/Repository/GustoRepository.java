package backend.backend_adprso.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import backend.backend_adprso.Entity.Items.GustoEntity;

@Repository
public interface GustoRepository extends JpaRepository<GustoEntity, Long>{
    @Query("SELECT g FROM GustoEntity g WHERE g.gust_id = :gustId")
    GustoEntity findByGust_id(Long gustId);
}
