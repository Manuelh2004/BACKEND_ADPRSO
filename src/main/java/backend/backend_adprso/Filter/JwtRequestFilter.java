package backend.backend_adprso.Filter;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import backend.backend_adprso.Service.AuthService.JwtUtil;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;   

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization header: " + authHeader);  

        String email = null;
        String jwt = null;
        String role = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            System.out.println("JWT token extraído: " + jwt);
            try {
                email = jwtUtil.extractUsername(jwt);
                role = Jwts.parserBuilder()
                        .setSigningKey(jwtUtil.getSigningKey())
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody()
                        .get("role", String.class);
                System.out.println("Email extraído del token: " + email);
                System.out.println("Role extraído del token: " + role);
            } catch (Exception e) {
                System.out.println("Error extrayendo datos del token: " + e.getMessage());
            }
        } else {
            System.out.println("No se encontró token con formato Bearer en Authorization header");
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateToken(jwt)) {
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
                System.out.println("Authorities asignadas: " + authorities);  

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("Autenticación establecida para: " + email + " con rol: " + role);
            }
        }

        chain.doFilter(request, response);
    }
}
