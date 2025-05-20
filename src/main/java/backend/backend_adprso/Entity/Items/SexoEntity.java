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
@Table(name = "sexo")
public class SexoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sex_id; 
    @Column
    private String sex_nombre;
}
