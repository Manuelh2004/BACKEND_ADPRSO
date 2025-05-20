package backend.backend_adprso.Entity.Usuario;

import java.time.LocalDate;

import backend.backend_adprso.Entity.Items.SexoEntity;
import backend.backend_adprso.Entity.Items.TipoDocumentoEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "usuario")
@Data
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario;
    @Column
    private String usr_nombre;
    @Column
    private String usr_apellido;
    @Column
    private String usr_documento;
    @Column
    private String usr_direccion;
    @Column
    private String usr_telefono;
    @Column
    private LocalDate usr_fecha_nacimiento;
    @Column(name = "usr_email", nullable = false, unique = true)
    private String email;    
    @Column
    private String usr_password;
    @ManyToOne
    @JoinColumn(name = "sex_id", nullable = false)
    private SexoEntity sexo;

    // Relación con TipoDocumentoEntity
    @ManyToOne
    @JoinColumn(name = "tipdoc_id", nullable = false)
    private TipoDocumentoEntity tipoDocumento;
}
