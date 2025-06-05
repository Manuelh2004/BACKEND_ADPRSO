package backend.backend_adprso.Entity.Mascota;

import com.fasterxml.jackson.annotation.JsonBackReference;

import backend.backend_adprso.Entity.Items.GustoEntity;
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
@Table(name = "gusto_mascota")
public class GustoMascotaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gumasc_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "masc_id", nullable = false)
    @JsonBackReference
    private MascotaEntity masc_id;

    @ManyToOne
    @JoinColumn(name = "gust_id", nullable = false)
    @JsonBackReference
    private GustoEntity gust_id;

}
