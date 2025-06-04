package backend.backend_adprso.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import backend.backend_adprso.Entity.Usuario.UsuarioEntity;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    @Query("SELECT u FROM UsuarioEntity u WHERE u.usr_email = :email")
    UsuarioEntity findByUsrEmail(String email);
}