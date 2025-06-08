package backend.backend_adprso.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.backend_adprso.Entity.Items.TipoDocumentoEntity;

@Repository
public interface TipoDocumentoRespository extends JpaRepository<TipoDocumentoEntity, Long> {
    
}
