package backend.backend_adprso.Service.Items;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.backend_adprso.Entity.Items.EstadoSaludEntity;
import backend.backend_adprso.Entity.Items.EstadoVacunaEntity;
import backend.backend_adprso.Entity.Items.GustoEntity;
import backend.backend_adprso.Entity.Items.NivelEnergiaEntity;
import backend.backend_adprso.Entity.Items.TamanioEntity;
import backend.backend_adprso.Entity.Items.TipoInteresadoEntity;
import backend.backend_adprso.Entity.Items.TipoMascotaEntity;
import backend.backend_adprso.Entity.Items.TipoPlanEntity;
import backend.backend_adprso.Repository.EstadoSaludRepository;
import backend.backend_adprso.Repository.EstadoVacunaRepository;
import backend.backend_adprso.Repository.GustoRepository;
import backend.backend_adprso.Repository.NivelEnergiaRepository;
import backend.backend_adprso.Repository.TamanioRepository;
import backend.backend_adprso.Repository.TipoInteresadoRepository;
import backend.backend_adprso.Repository.TipoMascotaRepository;
import backend.backend_adprso.Repository.TipoPlanRepository;

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
    TipoInteresadoRepository tipoInteresadoRepository;
    @Autowired
    TipoMascotaRepository tipoMascotaRepository;
    @Autowired
    TipoPlanRepository tipoPlanRepository;

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

    public List<TipoInteresadoEntity> ListarTipoInteresado() {
        return tipoInteresadoRepository.findAll();
    }

    public List<TipoMascotaEntity> ListarTipoMascota() {
        return tipoMascotaRepository.findAll();
    }

    public List<TipoPlanEntity> ListarTipoPlan() {
        return tipoPlanRepository.findAll();
    }   
}
