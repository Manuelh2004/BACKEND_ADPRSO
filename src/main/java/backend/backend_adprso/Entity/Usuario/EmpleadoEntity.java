package backend.backend_adprso.Entity.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Table;

@Entity
@DiscriminatorValue("EMPLEADO")
@Table(name = "empleado")
public class EmpleadoEntity extends UsuarioEntity {
    
  

    
}