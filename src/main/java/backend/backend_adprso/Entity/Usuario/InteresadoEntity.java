package backend.backend_adprso.Entity.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name ="interesado")
@Data
public class InteresadoEntity extends UsuarioEntity {
    
    @Column
    private String intEmail;
    @Column
    private String intTelefono;
    
}
