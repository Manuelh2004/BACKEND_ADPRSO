package backend.backend_adprso.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.backend_adprso.Entity.Items.SexoEntity;

public interface TipoDocumentoRespository extends JpaRepository<SexoEntity, Long> {
    
}
