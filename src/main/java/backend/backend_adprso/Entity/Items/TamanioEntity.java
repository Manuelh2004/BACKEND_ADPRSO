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
@Table(name = "tamanio")
public class TamanioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tam_id; 
    @Column
    private String tam_nombre;
}
