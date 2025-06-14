package backend.backend_adprso.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.backend_adprso.Entity.Items.ImagenEntity;

@Repository
public interface ImagenRepository extends JpaRepository<ImagenEntity, Long>{
    @Query("SELECT i FROM ImagenEntity i WHERE i.mascota.masc_id = :mascId")
    List<ImagenEntity> findByMascotaId(@Param("mascId") Long mascId);

    @Modifying
    @Query("DELETE FROM GustoMascotaEntity gm WHERE gm.masc_id.masc_id = :mascId")
    void deleteByMascotaId(Long mascId);
}
