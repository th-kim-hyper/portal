//package portal.config;
//
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class IpBlockService {
//
//    public String extractClientIp(HttpServletRequest request) {
//        String clientIp = request.getRemoteAddr();
//        // X-Forwarded-For 헤더가 있는 경우
//        String forwardedIp = request.getHeader("X-Forwarded-For");
//        if (forwardedIp != null && !forwardedIp.isEmpty()) {
//            clientIp = forwardedIp.split(",")[0].trim();
//        }
//
//        // IPv4 주소만 반환
//        if (clientIp.contains(":")) {
//            clientIp = "127.0.0.1"; // 기본값으로 로컬호스트 설정
//        }
//
//        return clientIp;
//    }
//
//    public boolean isIpBlocked(HttpServletRequest request) {
//        String clientIp = extractClientIp(request);
//        List<String> whitelistedIps = List.of(
//            "192.168.20.52","192.168.50.172",
//            "127.0.0.1"
//        );
//        log.info("#### clientIp : {}", clientIp);
//        return (whitelistedIps.contains(clientIp) != true);
//    }
//
//}
