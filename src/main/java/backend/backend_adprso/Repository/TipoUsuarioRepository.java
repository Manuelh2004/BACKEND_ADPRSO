package backend.backend_adprso.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.backend_adprso.Entity.Items.TipoUsuarioEntity;

public interface TipoUsuarioRepository extends JpaRepository <TipoUsuarioEntity, Long>{
    
}
