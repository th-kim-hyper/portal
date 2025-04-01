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
import org.springframework.security.web.SecurityFilterChain;
import portal.base.JsoupService;

import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

//    private final UserDetailsService userDetailsService;
    private final JsoupService jsoupService;
    private final ApplicationConfig applicationConfig;
    private final List<String> whitelistedIps = List.of(
        "192.168.20.52","192.168.50.172",
        "127.0.0.1"
    );
    private String[] whitelistArray = null;

    @PostConstruct
    public void init() {
        log.info("#### SecurityConfig init");
        List<String> whitelist = applicationConfig.portalProperties().getWhitelist();
        whitelistArray = whitelist.toArray(new String[0]);
        log.info("### whitelist({}) : {}", whitelistedIps.size(), whitelistedIps);
    }

@Bean
@Order(Ordered.HIGHEST_PRECEDENCE)
public SecurityFilterChain preSecurityFilterChain(HttpSecurity http) throws Exception {
    log.info("#### preSecurityFilterChain");
    http
        .securityMatcher(whitelistArray)
        .csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .headers(headersConfig -> headersConfig
            .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
        )
        .authorizeHttpRequests(authorizeRequests -> authorizeRequests
            .requestMatchers(whitelistArray).permitAll()
        )
    ;

    return http.build();
}

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) throws Exception {
        log.info("#### SecurityFilterChain");
        http
            .securityMatcher("/**")
//            .addFilterBefore(new IpBlockFilter(whitelistedIps), UsernamePasswordAuthenticationFilter.class)
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .headers(headersConfig -> headersConfig
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
            )
            .authenticationProvider(new CustomAuthenticationProvider(jsoupService))
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                // 권한그룹 별로 접근 허용
                .requestMatchers("/user/**").hasAnyAuthority(RoleGroup.USER_GROUP.getAuthorities())
                .requestMatchers("/admin/**").hasAnyAuthority(RoleGroup.ADMIN_GROUP.getAuthorities())
                .requestMatchers("/apiuser/**").hasAnyAuthority(RoleGroup.API_GROUP.getAuthorities())
                .requestMatchers("/apiadmin/**").hasAnyAuthority(RoleGroup.ADMIN_GROUP.getAuthorities())
//                // 개별 권한 별로 접근 허용
//                .requestMatchers("/user/temp/**").hasAnyAuthority(Role.TEMP.getAuthority(), Role.ADMIN.getAuthority())
//                .requestMatchers("/user/test/**").hasAnyAuthority(Role.TEST.getAuthority(), Role.ADMIN.getAuthority()                           )
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
                .successHandler(customAuthenticationSuccessHandler)
                .defaultSuccessUrl("/", false)
            )
            .logout((logoutConfig) ->
                logoutConfig
                    .deleteCookies()
                    .invalidateHttpSession(true)
                    .logoutSuccessUrl("/")
            )
//            .userDetailsService(userDetailsService)
        ;

        return http.build();
    }
}
