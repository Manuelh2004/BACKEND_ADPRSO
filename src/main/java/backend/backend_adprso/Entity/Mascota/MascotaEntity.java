package backend.backend_adprso.Entity.Mascota;

import java.time.LocalDate;

import backend.backend_adprso.Entity.Items.EstadoSaludEntity;
import backend.backend_adprso.Entity.Items.EstadoVacunaEntity;
import backend.backend_adprso.Entity.Items.NivelEnergiaEntity;
import backend.backend_adprso.Entity.Items.SexoEntity;
import backend.backend_adprso.Entity.Items.TamanioEntity;
import backend.backend_adprso.Entity.Items.TipoMascotaEntity;
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
@Table(name = "mascota")
public class MascotaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long masc_id; 
    @Column(nullable = false)
    private String masc_nombre;
    @Column
    private LocalDate masc_fecha_nacimiento;
    @Column
    private LocalDate masc_fecha_registro;
    @Column
    private String masc_historia; 
    @Column
    private String masc_observacion;   
    @Column
    private Integer masc_estado;

    @ManyToOne
    @JoinColumn(name = "sex_id", nullable = false)
    private SexoEntity sexo;
    @ManyToOne
    @JoinColumn(name = "tam_id", nullable = false)
    private TamanioEntity tamanio;
    @ManyToOne
    @JoinColumn(name = "nien_id", nullable = false)
    private NivelEnergiaEntity nivel_energia;
    @ManyToOne
    @JoinColumn(name = "tipma_id", nullable = false)
    private TipoMascotaEntity tipo_mascota;
    @ManyToOne
    @JoinColumn(name = "estsa_id", nullable = false)
    private EstadoSaludEntity estado_salud;
    @ManyToOne
    @JoinColumn(name = "estva_id", nullable = false)
    private EstadoVacunaEntity estado_vacuna;
}
