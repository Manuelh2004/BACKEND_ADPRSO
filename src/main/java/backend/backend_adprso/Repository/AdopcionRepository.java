package backend.backend_adprso.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.backend_adprso.Entity.Adopcion.AdopcionEntity;
import backend.backend_adprso.Entity.Mascota.MascotaEntity;
import backend.backend_adprso.Entity.Usuario.UsuarioEntity;

@Repository
public interface AdopcionRepository extends JpaRepository<AdopcionEntity, Long>{
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM AdopcionEntity a WHERE a.usuario = :usuario AND a.mascota = :mascota")
    boolean existsByUsuarioAndMascota(@Param("usuario") UsuarioEntity usuario, @Param("mascota") MascotaEntity mascota);

    @Query(value = "SELECT * FROM adopcion e WHERE e.adop_estado = :estado", nativeQuery = true)
    List<AdopcionEntity> findByEvenEstado(@Param("estado") Integer estado);

    List<AdopcionEntity> findByUsuario(UsuarioEntity usuario);
}   
