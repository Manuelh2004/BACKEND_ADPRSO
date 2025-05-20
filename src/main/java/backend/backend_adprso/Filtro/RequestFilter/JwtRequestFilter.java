package backend.backend_adprso.Filtro.RequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import backend.backend_adprso.Entity.Response.JwtUtil;
import backend.backend_adprso.Entity.Usuario.UsuarioEntity;
import backend.backend_adprso.Repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter  extends OncePerRequestFilter{
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization header: " + authHeader);

        String email = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            System.out.println("JWT token extraído: " + jwt);
            try {
                email = jwtUtil.extractUsername(jwt);
                System.out.println("Email extraído del token: " + email);
            } catch (Exception e) {
                System.out.println("Error extrayendo email del token: " + e.getMessage());
            }
        } else {
            System.out.println("No se encontró token con formato Bearer en Authorization header");
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Optional<UsuarioEntity> usuario = usuarioRepository.findByEmail(email);
            if (usuario.isPresent() && jwtUtil.validateToken(jwt)) {
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("Autenticación establecida para: " + email);
            } else {
                System.out.println("Empleado no encontrado o token inválido");
            }
        }

        chain.doFilter(request, response);
    }
}
