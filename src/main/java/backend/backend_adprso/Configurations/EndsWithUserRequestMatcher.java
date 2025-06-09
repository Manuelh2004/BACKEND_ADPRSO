package backend.backend_adprso.Configurations;

import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;

import jakarta.servlet.http.HttpServletRequest; // Usar jakarta.servlet en lugar de javax.servlet

public class EndsWithUserRequestMatcher {

    // Método que verifica si la URL termina con "/user"
    public boolean matches(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.endsWith("/public");
    }
}