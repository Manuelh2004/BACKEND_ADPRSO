package backend.backend_adprso.Entity.Mascota;

import java.util.List;

import lombok.Data;

@Data
public class MascotaImagenDTO {
    private Long mascotaId;          // Para almacenar el ID de la mascota
    private String mascotaNombre;    // Para almacenar el nombre de la mascota
    private List<String> imagenUrls; // Para almacenar las URLs de las imágenes

    // Constructor necesario para la consulta JPQL
    public MascotaImagenDTO(Long mascotaId, String mascotaNombre, List<String> imagenUrls) {
        this.mascotaId = mascotaId;
        this.mascotaNombre = mascotaNombre;
        this.imagenUrls = imagenUrls;
    }
}
