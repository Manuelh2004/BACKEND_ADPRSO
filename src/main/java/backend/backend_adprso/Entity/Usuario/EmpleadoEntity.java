package backend.backend_adprso.Entity.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "empleado")
public class EmpleadoEntity extends UsuarioEntity  {
    @Column
    private String emp_password; 
}