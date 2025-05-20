package backend.backend_adprso.Controller.AuthController;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
