package backend.backend_adprso.Entity.Items;

import com.fasterxml.jackson.annotation.JsonBackReference;

import backend.backend_adprso.Entity.Mascota.MascotaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "imagen")
public class ImagenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ima_id;
    @Column(name = "ima_url")
    private String imaUrl;

    @ManyToOne
    @JoinColumn(name = "masc_id", nullable = false)
    @JsonBackReference
    private MascotaEntity mascota;

    @Column(name = "ima_public_id")
    private String imaPublicId;
}
