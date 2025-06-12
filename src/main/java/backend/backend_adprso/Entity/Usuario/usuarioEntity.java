package backend.backend_adprso.Entity.Usuario;

import java.time.LocalDate;
import backend.backend_adprso.Entity.Items.TipoDocumentoEntity;
import backend.backend_adprso.Entity.Items.TipoUsuarioEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Table(name = "usuario")
@Data
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usr_id;
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
    @Column(nullable = false, unique = true)
    private String usr_email;   
    @Column
    private String usr_estado;
    @Column
    private String usr_password;
    
    @Transient// Banderita
    private boolean admin;  
    
    @ManyToOne
    @JoinColumn(name = "tipdoc_id", nullable = false)
    private TipoDocumentoEntity tipoDocumento;
    @ManyToOne
    @JoinColumn(name = "tipus_id", nullable = false)
    private TipoUsuarioEntity tipoUsuario;

    // Método para obtener el rol del tipoUsuario
    public String getRol() {
        return tipoUsuario != null ? tipoUsuario.getTipus_nombre() : null; // ROL_ADMIN o ROL_USER
    }

    
}
