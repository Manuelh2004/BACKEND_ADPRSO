package backend.backend_adprso.Controller.Auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
