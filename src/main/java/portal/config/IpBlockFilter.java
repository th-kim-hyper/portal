package portal.config;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
@WebFilter(urlPatterns = "/**", dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.ERROR})
public class IpBlockFilter extends OncePerRequestFilter {

    private final IpBlockService ipBlockService;
    private final DispatcherServlet dispatcherServlet;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("#### IpBlockFilter doFilterInternal");
        log.info("#### IpBlockFilter url : {}", request.getRequestURI());

        boolean isEndpointExist = isEndpointExist(request);
        if (!isEndpointExist) {
            log.warn("#### Invalid URL: {}", request.getRequestURI());
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid URL");
            return;
        }

        String clientIp = ipBlockService.extractClientIp(request);
        boolean isBlocked = ipBlockService.isIpBlocked(request);
        log.info("IP blocked: {} - {}", clientIp, isBlocked);
        if (isBlocked) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "접근이 차단된 IP입니다.");
            return;
        }
        filterChain.doFilter(request, response);
    }

    public boolean isEndpointExist(HttpServletRequest request) {

        List<HandlerMapping> handlerMappings = dispatcherServlet.getHandlerMappings();

        if (handlerMappings == null) {
            log.warn("HandlerMappings is null");
            return true; // 요구사항에 따라 true 반환
        }

        for (HandlerMapping handlerMapping : handlerMappings) {
            try {
                HandlerExecutionChain foundHandler = handlerMapping.getHandler(request);
                if (foundHandler != null) {

                    ResourceHttpRequestHandler resourceHttpRequestHandler = (ResourceHttpRequestHandler) foundHandler.getHandler();

                    log.info("#### Found handler : {}", foundHandler.getHandler());
                    log.info("#### Endpoint exists: {}", request.getRequestURI());
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

}