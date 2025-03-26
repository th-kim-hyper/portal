package portal.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class IpBlockFilter extends OncePerRequestFilter {

//    private final IpBlockService ipBlockService;
    private final List<String> whitelistedIps;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("#### IpBlockFilter doFilterInternal");
        log.info("#### IpBlockFilter url : {}", request.getRequestURI());

        String clientIp = extractClientIp(request);
        boolean isBlocked = isIpBlocked(request);
        log.info("IP blocked: {} - {}", clientIp, isBlocked);
        if (isBlocked) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "접근이 차단된 IP입니다.");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String extractClientIp(HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();
        // X-Forwarded-For 헤더가 있는 경우
        String forwardedIp = request.getHeader("X-Forwarded-For");
        if (forwardedIp != null && !forwardedIp.isEmpty()) {
            clientIp = forwardedIp.split(",")[0].trim();
        }

        // IPv4 주소만 반환
        if (clientIp.contains(":")) {
            clientIp = "127.0.0.1"; // 기본값으로 로컬호스트 설정
        }

        return clientIp;
    }

    private boolean isIpBlocked(HttpServletRequest request) {
        String clientIp = extractClientIp(request);
//        List<String> whitelistedIps = List.of(
//            "192.168.20.52","192.168.50.172",
//            "127.0.0.1"
//        );
        log.info("#### clientIp : {}", clientIp);
        return (whitelistedIps.contains(clientIp) != true);
    }

}