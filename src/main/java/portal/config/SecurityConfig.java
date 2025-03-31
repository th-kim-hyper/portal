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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

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

//    private List<String> whitelist = applicationConfig.portalProperties().getWhitelist();
//    private String[] whitelistArray = whitelist.toArray(new String[0]);

//    private final IpBlockService ipBlockService;

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        log.info("#### BCryptPasswordEncoder");
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
//        log.info("#### userDetailsService");
//        UserDetails userDetails = User.builder()
//            .username("guest")
//            .password("")
//            .roles("GUEST")
//            .build();
//
//        return new InMemoryUserDetailsManager(userDetails);
//    }
@Bean
@Order(Ordered.HIGHEST_PRECEDENCE)
public SecurityFilterChain preSecurityFilterChain(HttpSecurity http) throws Exception {
    log.info("#### preSecurityFilterChain");
//    List<String> whitelist = applicationConfig.portalProperties().getWhitelist();
//    String[] whitelistArray = whitelist.toArray(new String[0]);
//    log.info("### whitelist({}) : {}", whitelist.size(), whitelistArray);

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
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
            .authenticationProvider(new CustomAuthenticationProvider())
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                .requestMatchers("/user/**").hasAnyAuthority(Role.USER.getAuthority(), Role.ADMIN.getAuthority())
                .requestMatchers("/admin/**").hasAuthority(Role.ADMIN.getAuthority())
                .requestMatchers("/apiuser/**").hasAnyAuthority(Role.API_USER.getAuthority(), Role.ADMIN.getAuthority())
                .requestMatchers("/apiadmin/**").hasAuthority(Role.ADMIN.getAuthority())
                .anyRequest().authenticated()
            )
            .formLogin(form -> form.loginPage("/login").permitAll())
            .logout((logoutConfig) ->
                logoutConfig
                    .deleteCookies()
                    .invalidateHttpSession(true)
                    .logoutSuccessUrl("/")
            )
        ;

        return http.build();
    }
}
