package portal.config;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("#### SecurityFilterChain");

        List<String> whitelist = applicationConfig.portalProperties().getWhitelist();
        String[] whitelistArray = whitelist.toArray(new String[0]);
        CustomAuthenticationProvider customAuthenticationProvider = new CustomAuthenticationProvider();

        log.info("### whitelist({}) : {}", whitelist.size(), whitelistArray);

        http
//            .addFilterBefore(new IpBlockFilter(whitelistedIps), UsernamePasswordAuthenticationFilter.class)
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .headers(headersConfig -> headersConfig
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
            )
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers(whitelistArray).permitAll()
                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form.loginPage("/login").permitAll())
            .logout((logoutConfig) ->
                logoutConfig
                    .deleteCookies()
                    .invalidateHttpSession(true)
                    .logoutSuccessUrl("/")
            )
            .authenticationProvider(customAuthenticationProvider)
        ;

        return http.build();
    }
}
