package backend.backend_adprso.Entity.Adopcion;

import backend.backend_adprso.Entity.Mascota.MascotaEntity;
import lombok.Data;

@Data
public class AdopcionRequestDTO {
    private Long adop_id;

    private String adop_motivo;

    private MascotaEntity mascota;
}
