package backend.backend_adprso.Entity.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "empleado")
@Data
public class EmpleadoEntity extends UsuarioEntity  {

    @Column
    private String empRol;
    
    @Column
    private String empPassword;
  
}