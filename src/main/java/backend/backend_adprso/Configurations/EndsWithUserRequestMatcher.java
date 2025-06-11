package backend.backend_adprso.Configurations;

import jakarta.servlet.http.HttpServletRequest; 

public class EndsWithUserRequestMatcher {

    public boolean matches(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.endsWith("/public");
    }
}