package backend.backend_adprso.Entity.Usuario;
import backend.backend_adprso.Entity.Items.TipoInteresadoEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name ="interesado")
public class InteresadoEntity extends UsuarioEntity {
    @Column
    private Integer inter_estado;
    @ManyToOne
    @JoinColumn(name = "tipin_id", nullable = false)
    private TipoInteresadoEntity tipoInteresado;    
}
