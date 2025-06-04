package backend.backend_adprso.Entity.Adopcion;

import java.time.LocalDate;

import backend.backend_adprso.Entity.Mascota.MascotaEntity;
import backend.backend_adprso.Entity.Usuario.UsuarioEntity;
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
@Table(name = "adopcion")
public class AdopcionEntity {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adop_id; 
    @Column(nullable = false)
    private String adop_motivo;
    @Column
    private LocalDate adop_fecha;
    @Column
    private Integer adop_estado;

    @ManyToOne
    @JoinColumn(name = "masc_id", nullable = false)
    private MascotaEntity mascota;
    @ManyToOne
    @JoinColumn(name = "usr_id", nullable = false)
    private UsuarioEntity usuario;
}
