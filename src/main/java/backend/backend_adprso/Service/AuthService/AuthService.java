package backend.backend_adprso.Service.AuthService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.backend_adprso.Entity.Usuario.EmpleadoEntity;
import backend.backend_adprso.Repository.EmpleadoRepository;

@Service
public class AuthService {
      @Autowired
    private EmpleadoRepository empleadoRepository;

    public Optional<EmpleadoEntity> login(String email, String password) {
    Optional<EmpleadoEntity> empleadoOpt = empleadoRepository.findByEmail(email);
    if (empleadoOpt.isPresent()) {
        EmpleadoEntity empleado = empleadoOpt.get();
        if (empleado.getEmp_password().equals(password)) {
            return Optional.of(empleado);
        }
    }
        return Optional.empty();
    }

}
