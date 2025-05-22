package backend.backend_adprso.Entity.Items;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "raza")
public class RazaEntity {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long raz_id; 
    @Column
    private String raz_nombre;

    @ManyToOne
    @JoinColumn(name = "tipma_id", nullable = false)
    private TipoMascotaEntity tipoMascota;
}
