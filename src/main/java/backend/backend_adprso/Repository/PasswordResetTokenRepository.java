package backend.backend_adprso.Repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.backend_adprso.Entity.Usuario.PasswordResetTokenEntity;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {

    // Consulta personalizada utilizando JPQL
    @Query("SELECT p FROM PasswordResetTokenEntity p WHERE p.token = :token")
    Optional<PasswordResetTokenEntity> findByToken(@Param("token") String token);
}