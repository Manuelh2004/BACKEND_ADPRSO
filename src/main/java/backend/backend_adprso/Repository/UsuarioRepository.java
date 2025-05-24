package backend.backend_adprso.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.backend_adprso.Entity.Usuario.UsuarioEntity;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
   @Query(value = "SELECT * FROM usuario u WHERE u.usr_email = :email", nativeQuery = true)
    Optional<UsuarioEntity> findByUsrEmail(@Param("email") String email);
}