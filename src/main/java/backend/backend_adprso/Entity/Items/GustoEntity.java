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
@Table(name = "gusto") // Para los perritos
public class GustoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gust_id; 
    @Column
    private String gust_nombre;
}
