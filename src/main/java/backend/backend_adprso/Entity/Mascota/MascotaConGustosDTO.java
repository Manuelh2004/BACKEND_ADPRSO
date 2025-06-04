package backend.backend_adprso.Entity.Mascota;

import java.util.List;

import lombok.Data;

@Data
public class MascotaConGustosDTO {
    private MascotaEntity mascota;
    private List<Long> gustosIds;
    private List<String> imagenUrls; // Lista de URLs de imágenes

}
