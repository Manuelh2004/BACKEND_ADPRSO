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
@Table(name = "tipo_interesado") // adoptante, interesado, voluntario
public class TipoInteresadoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tipin_id; 
    @Column
    private String tipin_nombre;
}
