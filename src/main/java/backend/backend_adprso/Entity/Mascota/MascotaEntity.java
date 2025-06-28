package backend.backend_adprso.Entity.Mascota;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import backend.backend_adprso.Entity.Items.EstadoSaludEntity;
import backend.backend_adprso.Entity.Items.EstadoVacunaEntity;
import backend.backend_adprso.Entity.Items.ImagenEntity;
import backend.backend_adprso.Entity.Items.NivelEnergiaEntity;
import backend.backend_adprso.Entity.Items.SexoEntity;
import backend.backend_adprso.Entity.Items.TamanioEntity;
import backend.backend_adprso.Entity.Items.TipoMascotaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "mascota")
public class MascotaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long masc_id; 
    @Column(nullable = false)
    private String masc_nombre;
    @Column
    private LocalDate masc_fecha_nacimiento;
    @Column
    private LocalDate masc_fecha_registro;
    @Column
    private String masc_historia; 
    @Column
    private String masc_observacion;   
    @Column
    private Integer masc_estado;

    @ManyToOne
    @JoinColumn(name = "sex_id", nullable = false)
    private SexoEntity sexo;
    @ManyToOne
    @JoinColumn(name = "tam_id", nullable = false)
    private TamanioEntity tamanio;
    @ManyToOne
    @JoinColumn(name = "nien_id", nullable = false)
    private NivelEnergiaEntity nivel_energia;
    @ManyToOne
    @JoinColumn(name = "tipma_id", nullable = false)
    private TipoMascotaEntity tipo_mascota;
    @ManyToOne
    @JoinColumn(name = "estsa_id", nullable = false)
    private EstadoSaludEntity estado_salud;
    @ManyToOne
    @JoinColumn(name = "estva_id", nullable = false)
    private EstadoVacunaEntity estado_vacuna;

    @OneToMany(mappedBy = "masc_id")
    @JsonIgnore
    private List<GustoMascotaEntity> gustoMascotaList;

    @OneToMany(mappedBy = "mascota")
    @JsonManagedReference
    private List<ImagenEntity> imagenes;

    public List<Map<String, Object>> getGustoNames() {
        List<Map<String, Object>> gustoInfoList = new ArrayList<>();
        if (gustoMascotaList != null) {
            for (GustoMascotaEntity gustoMascota : gustoMascotaList) {
                if (gustoMascota.getGust_id() != null) {
                    // Creamos un mapa con el id y nombre del gusto
                    Map<String, Object> gustoMap = new HashMap<>();
                    gustoMap.put("id", gustoMascota.getGust_id().getGust_id());
                    gustoMap.put("nombre", gustoMascota.getGust_id().getGust_nombre());
                    gustoInfoList.add(gustoMap);
                }
            }
        }
        return gustoInfoList;
    }
}
