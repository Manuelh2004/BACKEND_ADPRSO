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

@Entity
@Table(name = "apadrinamiento")
@Data
public class ApadrinamientoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plap_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "apo_id", nullable = false)
    private ApoyoEconomicoEntity apoyo;

    @ManyToOne
    @JoinColumn(name = "masc_id", nullable = false)
    private MascotaEntity mascota;

    @ManyToOne
    @JoinColumn(name = "tiplan_id", nullable = false)
    private TipoPlanEntity tipoPlan;

    @Column(name = "plap_descripcion")
    private String descripcion;

    @Column(name = "plap_estado")
    private String estado;
}
