//package portal.config;
//
//import jakarta.servlet.DispatcherType;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.servlet.DispatcherServlet;
//
//import java.util.List;
//
//@Slf4j
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final ApplicationConfig applicationConfig;
//    private final IpBlockService ipBlockService;
//    private final DispatcherServlet dispatcherServlet;
//
////    @Bean
////    public PasswordEncoder passwordEncoder() {
////        log.info("#### BCryptPasswordEncoder");
////        return new BCryptPasswordEncoder();
////    }
////
////    @Bean
////    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
////        log.info("#### userDetailsService");
////        UserDetails userDetails = User.builder()
////            .username("guest")
////            .password("")
////            .roles("GUEST")
////            .build();
////
////        return new InMemoryUserDetailsManager(userDetails);
////    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        log.info("#### SecurityFilterChain");
//
//        List<String> whitelist = applicationConfig.portalProperties().getWhitelist();
//        String[] whitelistArray = whitelist.toArray(new String[0]);
////        CustomAuthenticationProvider customAuthenticationProvider = new CustomAuthenticationProvider();
//
//        log.info("### whitelist({}) : {}", whitelist.size(), whitelistArray);
//
//        http
//            .csrf(AbstractHttpConfigurer::disable)
//            .cors(AbstractHttpConfigurer::disable)
//            .httpBasic(AbstractHttpConfigurer::disable)
//            .headers(headersConfig -> headersConfig
//                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
//            )
////            .sessionManagement(session -> session
////                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
////            )
//            // static resources handling
//
//
//            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
//                .requestMatchers(whitelistArray).permitAll()
////                    .requestMatchers("/","/error").permitAll()
////                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
//                .anyRequest().authenticated()
//            )
////            .formLogin(form -> form.loginPage("/login").permitAll())
////            .logout((logoutConfig) ->
////                logoutConfig
////                    .deleteCookies()
////                    .invalidateHttpSession(true)
////                    .logoutSuccessUrl("/")
////            )
//
//            .exceptionHandling(
//                exceptionHandling -> exceptionHandling
////                    .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
//                    .accessDeniedPage("/public/403.html")
////                    .accessDeniedHandler(new CustomAccessDeniedHandler())
//            )
////            .authenticationProvider(customAuthenticationProvider)
////            .addFilterBefore(new IpBlockFilter(ipBlockService, dispatcherServlet), UsernamePasswordAuthenticationFilter.class)
////            .addFilterBefore(new RequestFilter(dispatcherServlet), UsernamePasswordAuthenticationFilter.class)
//        ;
//
//        return http.build();
//    }
//}
