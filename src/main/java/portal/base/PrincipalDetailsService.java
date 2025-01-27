//package portal.base;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class PrincipalDetailsService implements UserDetailsService {
//
//    @Override
//    public UserDetails loadUserByUsername(String username) {
//        log.info("#### loadUserByUsername : {}", username);
//        return User.builder()
//                .username(username)
//                .password("password")
//                .roles("USER")
//                .build();
//    }
//}