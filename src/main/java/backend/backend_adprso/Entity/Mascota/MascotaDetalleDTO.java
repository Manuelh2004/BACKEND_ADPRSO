package backend.backend_adprso.Entity.Mascota;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MascotaDetalleDTO {

        private Long masc_id;

        private String masc_nombre;

        private LocalDate masc_fecha_nacimiento;

        private LocalDate masc_fecha_registro;

        private String masc_historia;

        private String masc_observacion;

        private String masc_estado;

        private String sexo;

        private String tamanio;

        private String nivel_energia;

        private String tipo_mascota;

        private String estado_salud;

        private String estado_vacuna;

        private List<String> masc_gustos;

        private List<String> imagenes_url;

}
