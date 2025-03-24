//package portal.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.function.Function;
//
//@Slf4j
//public class CustomAuthenticationProvider implements AuthenticationProvider {
//
//    private Function<Authentication, Authentication> authenticationFunction = null;
//
//    public void SetAuthenticationFunction(Function<Authentication, Authentication> authenticationFunction) {
//        this.authenticationFunction = authenticationFunction;
//    }
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//
//        if(authenticationFunction != null) {
//            return authenticationFunction.apply(authentication);
//        }
//
//        String username = authentication.getName();
//        String password = (String) authentication.getCredentials();
//        String authority = (username.equals("admin") ? "ROLE_ADMIN" : "ROLE_USER");
//
//        log.info("#### authenticate : {} / {} / {}", username, password, authority);
//
//        UserDetails user = User
//            .builder()
//            .username(username)
//            .password(password)
//            .authorities(authority)
//            .build();
//
//        // TODO: Implement your own logic for authentication
//        log.warn("#### Implement your own logic for authentication");
//
//        return new UsernamePasswordAuthenticationToken(username, password, user.getAuthorities());
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        log.info("#### supports : {}", authentication);
//        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
//    }
//}