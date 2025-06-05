package backend.backend_adprso.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.backend_adprso.Entity.Mascota.GustoMascotaEntity;

@Repository
public interface GustoMascotaRepository extends JpaRepository <GustoMascotaEntity, Long>{
    @Query("SELECT gm FROM GustoMascotaEntity gm WHERE gm.masc_id.masc_id = :mascId")
    List<GustoMascotaEntity> findByMascotaId(@Param("mascId") Long mascId);

    @Modifying
    @Query("DELETE FROM GustoMascotaEntity gm WHERE gm.masc_id.masc_id = :mascId")
    void deleteByMascId(Long mascId);
}
