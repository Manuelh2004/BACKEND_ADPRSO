package backend.backend_adprso.Entity.Evento;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "evento")
public class EventoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long even_id; 
    @Column(nullable = false)
    private String even_nombre;
    @Column
    private String even_descripcion;
    @Column
    private LocalDate even_fecha_inicio;
    @Column
    private LocalDate even_fecha_fin;
    @Column
    private String even_lugar;
    @Column
    private String even_imagen;
    @Column
    private Integer even_estado;
    @Column
    private String even_imagen_public_id;
}
