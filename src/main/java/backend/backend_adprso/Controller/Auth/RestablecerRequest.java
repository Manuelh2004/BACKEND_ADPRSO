package backend.backend_adprso.Controller.Auth;

import lombok.Data;

@Data
public class RestablecerRequest {
    private String email;
    private String codigo;
    private String nuevaPassword;
}
