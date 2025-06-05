package backend.backend_adprso.Controller.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.backend_adprso.Service.Usuario.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
   
    @Autowired
    UsuarioService usuarioService;

   
   
}
