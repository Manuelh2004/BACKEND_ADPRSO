package backend.backend_adprso.Service.Items;

import java.util.List;

import backend.backend_adprso.Entity.Items.*;
import backend.backend_adprso.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {
    @Autowired
    EstadoSaludRepository estadoSaludRepository;
    @Autowired
    EstadoVacunaRepository estadoVacunaRepository; 
    @Autowired
    GustoRepository gustoRepository; 
    @Autowired 
    NivelEnergiaRepository nivelEnergiaRepository; 
    @Autowired
    TamanioRepository tamanioRepository;
    @Autowired
    TipoMascotaRepository tipoMascotaRepository;
    @Autowired
    TipoPlanRepository tipoPlanRepository;
    @Autowired
    TipoUsuarioRepository tipoUsuarioRepository;
    @Autowired
    TipoDocumentoRespository tipoDocumentoRepository;
    @Autowired
    SexoRepository sexoRepository;

    public List<EstadoSaludEntity> ListarEstadoSalud() {
        return estadoSaludRepository.findAll();
    }

     public List<EstadoVacunaEntity> ListarEstadoVacuna() {
        return estadoVacunaRepository.findAll();
    }

    public List<GustoEntity> ListarGustos() {
        return gustoRepository.findAll();
    }

    public List<NivelEnergiaEntity> ListarNivelEnergia() {
        return nivelEnergiaRepository.findAll();
    }

    // ******************************************************************************      
    public List<TamanioEntity> ListarTamanios() {
        return tamanioRepository.findAll();
    }

    public List<TipoMascotaEntity> ListarTipoMascota() {
        return tipoMascotaRepository.findAll();
    }

    public List<TipoPlanEntity> ListarTipoPlan() {
        return tipoPlanRepository.findAll();
    }   

    public List<TipoUsuarioEntity> ListarTipoUsuario() {
        return tipoUsuarioRepository.findAll();
    }

    public List<TipoDocumentoEntity> ListarTipoDocumento() {
        return tipoDocumentoRepository.findAll();
    }

    public List<SexoEntity> ListarSexo() {
        return sexoRepository.findAll();
    }

    public TipoDocumentoEntity getTipoDocumentoById(Long id) {
        return tipoDocumentoRepository.findById(id).orElse(null);  // Retorna el tipo de documento o null si no se encuentra
    }

    public TipoUsuarioEntity getTipoUsuarioById(Long id) {
        return tipoUsuarioRepository.findById(id).orElse(null);  // Retorna el tipo de usuario o null si no se encuentra
    }

}
