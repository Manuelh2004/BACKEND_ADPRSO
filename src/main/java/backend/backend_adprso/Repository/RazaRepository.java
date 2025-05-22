package backend.backend_adprso.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.backend_adprso.Entity.Items.RazaEntity;

@Repository
public interface RazaRepository extends JpaRepository<RazaEntity, Long> {
    @Query(value = "SELECT * FROM raza r WHERE r.tipma_id = :tipoMascotaId", nativeQuery = true)
    List<RazaEntity> findByTipoMascotaId(@Param("tipoMascotaId") Long tipoMascotaId);
}