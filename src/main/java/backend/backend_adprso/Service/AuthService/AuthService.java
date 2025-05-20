package backend.backend_adprso.Service.AuthService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.backend_adprso.Entity.Response.JwtUtil;
import backend.backend_adprso.Entity.Usuario.usuarioEntity;
import backend.backend_adprso.Repository.UsuarioRepository;
@Service
public class AuthService {
     @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public String login(String email, String password) {
        Optional<usuarioEntity> usuario = usuarioRepository.findByEmail(email);
        if (usuario.isPresent() && usuario.get().getUsr_password().equals(password)) {
            return jwtUtil.generateToken(email);
        } else {
            throw new RuntimeException("Credenciales inválidas");
        }
    }
}