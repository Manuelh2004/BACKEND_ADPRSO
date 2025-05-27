package backend.backend_adprso.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import backend.backend_adprso.Entity.Mascota.MascotaEntity;

public interface MascotaRepository extends JpaRepository<MascotaEntity, Long> {
    @Query("SELECT m FROM MascotaEntity m WHERE m.masc_estado = :estado")
    List<MascotaEntity> findByMascEstado(@Param("estado") Integer estado);
}
