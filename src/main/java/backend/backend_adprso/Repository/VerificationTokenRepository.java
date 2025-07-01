package backend.backend_adprso.Repository;

import backend.backend_adprso.Entity.Usuario.VerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationTokenEntity, Long> {
     @Query("SELECT v FROM VerificationTokenEntity v WHERE v.token = :token")
    Optional<VerificationTokenEntity> findByToken(@Param("token") String token);
}