package backend.backend_adprso.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.backend_adprso.Entity.Items.ImagenEntity;
import jakarta.transaction.Transactional;

@Repository
public interface ImagenRepository extends JpaRepository<ImagenEntity, Long>{
    @Query("SELECT i FROM ImagenEntity i WHERE i.mascota.masc_id = :mascId")
    List<ImagenEntity> findByMascotaId(@Param("mascId") Long mascId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ImagenEntity i WHERE i.mascota.masc_id = :mascId")
    void deleteByMascotaId(@Param("mascId") Long mascId);  // Eliminar imágenes por mascota

    ImagenEntity findByImaPublicId(String url);
}
