//package portal.config;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import java.util.List;
//
//@Slf4j
//public class CustomAuthFilter extends UsernamePasswordAuthenticationFilter {
//
//    public CustomAuthFilter(AuthenticationManager authenticationManager) {
//        log.info("#### CustomAuthFilter");
//        super.setAuthenticationManager(authenticationManager);
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");
//
//        log.info("Attempting authentication for user / password: {} / {}", username, password);
//
//        List<GrantedAuthority> authorities;
//        if(username.equals("admin")) {
//            authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
//        } else {
//            authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
//        }
//
//        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password, authorities);
//        authRequest.setDetails(authorities);
//
////        return this.getAuthenticationManager().authenticate(authRequest);
//        return authRequest;
//
////        if(username.equals("admin")) {
////            authRequest.setDetails("ROLE_ADMIN");
////        } else {
////            authRequest.setDetails("ROLE_USER");
////        }
////
////        authRequest.setAuthenticated(true);
////
////        return this.getAuthenticationManager().authenticate(authRequest);
//    }
//
////    @Override
////    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
////        AuthenticationException {
////        Authentication authentication = super.attemptAuthentication(request, response);
////        User user = (User) authentication.getPrincipal();
////        if (user.getUsername().startsWith("test")) {
////            // 테스트 유저인 경우 어드민과 유저 권한 모두 부여
////            return new UsernamePasswordAuthenticationToken(
////                user,
////                null,
////                Stream.of("ROLE_ADMIN", "ROLE_USER")
////                    .map(authority -> (GrantedAuthority) () -> authority)
////                    .collect(Collectors.toList())
////            );
////        }
////        return authentication;
////    }
//
//
//}