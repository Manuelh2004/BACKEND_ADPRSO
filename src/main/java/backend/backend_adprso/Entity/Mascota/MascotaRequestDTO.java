package backend.backend_adprso.Entity.Mascota;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MascotaRequestDTO {
    private String nombre;
    private LocalDate fechaNacimiento;
    private String historia;
    private String observacion;
    private Long sexoId;
    private Long tamanioId;
    private Long nivelEnergiaId;
    private Long tipoMascotaId;
    private Long estadoSaludId;
    private Long estadoVacunaId;
    private List<Long> gustoIds;
    private List<String> imagenesUrls;
}
