package backend.backend_adprso.Entity.Items;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "estado_salud")
public class EstadoSaludEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long estsa_id; 
    @Column
    private String estsa_nombre;
}
