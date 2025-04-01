//package portal.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        log.info("#### loadUserByUsername : {}", username);
//        User user = (User) User.builder()
//            .username(username)
//            .password("password") // Replace with actual password retrieval logic
//            .authorities(Role.USER.getAuthority())
//            .build();
//        return new CustomUserDetails(user);
//    }
//}
