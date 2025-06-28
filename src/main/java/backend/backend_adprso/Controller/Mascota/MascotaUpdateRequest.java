package backend.backend_adprso.Controller.Mascota;

import java.util.List;

import backend.backend_adprso.Entity.Mascota.MascotaEntity;

public class MascotaUpdateRequest {

    private MascotaEntity mascota;   // Para los detalles de la mascota
    private List<String> imagenUrls; // Para las URLs de las imágenes
    private List<Long> newGustos;    // Para los IDs de los nuevos gustos

    // Getters y setters
    public MascotaEntity getMascota() {
        return mascota;
    }

    public void setMascota(MascotaEntity mascota) {
        this.mascota = mascota;
    }

    public List<String> getImagenUrls() {
        return imagenUrls;
    }

    public void setImagenUrls(List<String> imagenUrls) {
        this.imagenUrls = imagenUrls;
    }

    public List<Long> getNewGustos() {
        return newGustos;
    }

    public void setNewGustos(List<Long> newGustos) {
        this.newGustos = newGustos;
    }
}

