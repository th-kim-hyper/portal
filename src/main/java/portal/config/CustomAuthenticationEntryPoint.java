package portal.config;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        if (!isEndpointExist(request)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }

    private boolean isEndpointExist(HttpServletRequest request) {
        RequestDispatcher dispatcher = request.getRequestDispatcher(request.getRequestURI());
        return dispatcher != null;
    }
}