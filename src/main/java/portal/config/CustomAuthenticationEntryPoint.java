//package portal.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//
//@Slf4j
//@Component
//public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//        log.info("#### CustomAuthenticationEntryPoint : {}", authException.getMessage());
//        response.contentType = MediaType.APPLICATION_JSON_VALUE;
//        response.characterEncoding = "UTF-8";
//        val writer = response.writer
//        val objectMapper = ObjectMapper()
//        writer.write(objectMapper.writeValueAsString(ApiResponse.error("페이지를 찾을 수 없습니다.")))
//        writer.flush()
//        // 404 not found
////        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
////        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
////        response.setCharacterEncoding("UTF-8");
////        PrintWriter writer = response.getWriter();
////        ObjectMapper objectMapper = new ObjectMapper();
////        writer.write(objectMapper.writeValueAsString("페이지를 찾을 수 없습니다."));
////        writer.flush();
//    }
//}
