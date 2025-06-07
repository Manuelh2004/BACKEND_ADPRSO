package backend.backend_adprso.Service.Items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.backend_adprso.Entity.Items.TipoDocumentoEntity;
import backend.backend_adprso.Repository.TipoDocumentoRepository;


@Service
public class TipoDocumentoService {

    @Autowired
    TipoDocumentoRepository TipoDocumentoRepository;

     public TipoDocumentoEntity getTipoDocumentoById(Long id) {
        return TipoDocumentoRepository.findById(id).orElse(null);  // Retorna el tipo de documento o null si no se encuentra
    }
}
