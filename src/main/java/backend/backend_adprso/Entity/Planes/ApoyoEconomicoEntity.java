package backend.backend_adprso.Entity.Planes;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.Id;

@Entity
@Table(name = "apoyo_economico")
@Data
public class ApoyoEconomicoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long apo_id;
    @Column
    private String apo_nombre;
    @Column
    private Double apo_monto;
    @Column
    private LocalDate apo_fecha;
    @Column
    private String apo_cod_aprobacion;
    @Column
    private String apo_mensaje;
    @Column
    private String apo_estado;
}
