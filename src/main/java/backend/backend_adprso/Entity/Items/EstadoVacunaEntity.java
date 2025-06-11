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
@Table(name = "estado_vacuna") // Vacunas completa, imcompleta etc
public class EstadoVacunaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long estva_id; 
    @Column
    private String estva_nombre;
    @Column
    private String estva_descripcion;
}
