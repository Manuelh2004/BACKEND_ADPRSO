package backend.backend_adprso.Entity.Evento;

import backend.backend_adprso.Entity.Usuario.UsuarioEntity;
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
@Table(name = "evento_usuario")
public class EventoUsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long evein_id; 
    @ManyToOne
    @JoinColumn(name = "even_id", nullable = false)
    private EventoEntity evento;
    @ManyToOne
    @JoinColumn(name = "usr_id", nullable = false)
    private UsuarioEntity usuario;
}
