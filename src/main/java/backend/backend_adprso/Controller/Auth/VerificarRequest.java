package backend.backend_adprso.Controller.Auth;

import lombok.Data;

@Data
public class VerificarRequest {
    private String email;
    private int codigo;

}