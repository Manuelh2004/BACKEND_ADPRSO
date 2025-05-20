package backend.backend_adprso.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.backend_adprso.Entity.Usuario.usuarioEntity;

@Repository
public interface UsuarioRepository extends JpaRepository<usuarioEntity, Long>{
     Optional<usuarioEntity> findByEmail(String email);

}
