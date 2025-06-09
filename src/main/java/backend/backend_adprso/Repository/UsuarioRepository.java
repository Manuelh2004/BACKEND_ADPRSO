package backend.backend_adprso.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.backend_adprso.Entity.Usuario.UsuarioEntity;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    @Query(value = "SELECT u.* FROM usuario u WHERE u.tipus_id = :tipus_id", nativeQuery = true)
    List<UsuarioEntity> findByTipoUsuario_Tipus_id(@Param("tipus_id") Long tipus_id);

   @Query("SELECT u FROM UsuarioEntity u WHERE u.usr_email = :email")
    Optional<UsuarioEntity> findByUsrEmail(String email);

}