package backend.backend_adprso.Entity.Planes;

import backend.backend_adprso.Entity.Items.TipoPlanEntity;
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
@Table(name = "apadrinamiento")
public class ApadrinamientoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plap_id;    
    @Column
    private String plap_descripcion;
    @Column
    private String plap_estado;

    @ManyToOne
    @JoinColumn(name = "masc_id", nullable = false)
    private MascotaEntity mascota;
    @ManyToOne
    @JoinColumn(name = "tiplan_id", nullable = false)
    private TipoPlanEntity tipoPlan;
}
