package portal.config;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import portal.base.MailplugService;
import portal.base.RpaService;

import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MailplugService mailplugService;
    private final RpaService rpaService;
    private final ApplicationConfig applicationConfig;
    private String[] publicPathArray = null;
    private Boolean ipBlockEnable = false;
    private List<String> ipWhitelist = null;
    private List<String> ipBlacklist = null;

    @PostConstruct
    public void init() {
        log.info("#### SecurityConfig init");
        var portalProperties = applicationConfig.portalProperties();
 
        List<String> publicPaths = portalProperties.getPublicPaths();
        publicPathArray = publicPaths.toArray(new String[0]);
        log.info("#### publicPaths({}) : {}", publicPaths.size(), publicPaths);
 
        var ipBlock = portalProperties.getIpBlock();
        ipBlockEnable = ipBlock.getEnable();
        log.info("#### ipBlockEnable : {}", ipBlockEnable);
 
        if (ipBlockEnable) {
            ipWhitelist = ipBlock.getWhitelist();
            ipBlacklist = ipBlock.getBlacklist();
        }
    }

    @Bean
    public SecurityFilterChain simpleFilterChain(HttpSecurity http) throws Exception {
        log.info("#### simpleFilterChain");
        http
           .securityMatcher(publicPathArray)
//            .csrf(AbstractHttpConfigurer::disable)
//            .cors(cors -> cors
//                .configurationSource(corsConfigurationSource())
//            )
//            .httpBasic(AbstractHttpConfigurer::disable)
//            .headers(headersConfig -> headersConfig
//                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
//            )
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                .anyRequest().permitAll()
            );

        return http.build();
    }

//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOriginPatterns(List.of("*")); // 모든 도메인 허용
//        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 모든 HTTP 메서드 허용
//        configuration.setAllowedHeaders(List.of("*")); // 모든 헤더 허용
//        configuration.setAllowCredentials(true); // 인증 정보 포함 허용
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new InMemoryUserDetailsManager();
//    }
//
//    @Bean
//    public CustomAuthenticationProvider customAuthenticationProvider() {
//        log.info("#### create customAuthenticationProvider bean");
//        return new CustomAuthenticationProvider(mailplugService, rpaService);
//    }

//    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    public SecurityFilterChain preSecurityFilterChain(HttpSecurity http) throws Exception {
//        log.info("#### preSecurityFilterChain");
//
//        // IP 차단 필터 추가
//        if(ipBlockEnable){
//            log.info("#### IpBlockFilter 추가");
//            log.info("#### ipBlacklist({}) : {}", ipBlacklist.size(), ipBlacklist);
//            log.info("#### ipWhitelist({}) : {}", ipWhitelist.size(), ipWhitelist);
//            http.addFilterBefore(new IpBlockFilter(ipBlacklist, ipWhitelist), UsernamePasswordAuthenticationFilter.class);
//        } else {
//            log.info("#### IpBlockFilter 사용안함");
//        }
//
//        // publicPath에 대한 접근 허용
//        http
//            .securityMatcher(publicPathArray)
//            .csrf(AbstractHttpConfigurer::disable)
//            .cors(cors -> cors
//                .configurationSource(corsConfigurationSource())
//            )
//            .httpBasic(AbstractHttpConfigurer::disable)
//            .headers(headersConfig -> headersConfig
//                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
//            )
//            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
//                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
//                .anyRequest().permitAll()
//            );
//
//        return http.build();
//    }
//
//    @Bean
//    @Order(2)
//    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) throws Exception {
//        log.info("#### SecurityFilterChain");
//        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter();
//
//        http
//            .securityMatcher("/**")
////            .csrf(AbstractHttpConfigurer::disable)
////            .cors(cors -> cors
////                .configurationSource(corsConfigurationSource())
////            )
////            .httpBasic(AbstractHttpConfigurer::disable)
////            .headers(headersConfig -> headersConfig
////                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
////            )
//            .addFilterAt(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//            .authenticationProvider(customAuthenticationProvider())
//            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
//                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
//                // 권한그룹 별로 접근 허용
//                .requestMatchers("/user/**").hasAnyAuthority(RoleGroup.USER_GROUP.getAuthorities())
//                .requestMatchers("/admin/**").hasAnyAuthority(RoleGroup.ADMIN_GROUP.getAuthorities())
//                .requestMatchers("/apiuser/**").hasAnyAuthority(RoleGroup.API_GROUP.getAuthorities())
//                .requestMatchers("/apiadmin/**").hasAnyAuthority(RoleGroup.ADMIN_GROUP.getAuthorities())
////                // 개별 권한 별로 접근 허용
////                .requestMatchers("/user/temp/**").hasAnyAuthority(Role.TEMP.getAuthority(), Role.ADMIN.getAuthority())
////                .requestMatchers("/user/test/**").hasAnyAuthority(Role.TEST.getAuthority(), Role.ADMIN.getAuthority()                           )
//                .anyRequest().authenticated()
//            )
//            .formLogin(form -> form
//                .loginPage("/login")
//                .permitAll()
//                .successHandler(customAuthenticationSuccessHandler)
//                .defaultSuccessUrl("/", false)
//                .failureUrl("/login?error=true")
//            )
//            .logout((logoutConfig) ->
//                logoutConfig
//                    .deleteCookies()
//                    .invalidateHttpSession(true)
//                    .logoutSuccessUrl("/")
//            )
////            .userDetailsService(userDetailsService)
//        ;
//
//        return http.build();
//    }
}
