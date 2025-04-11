package portal.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public CustomAuthenticationFilter() {
        super();
        log.info("#### CustomAuthenticationFilter default constructor");
    }

//    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
//        super(authenticationManager);
//        log.info("#### CustomAuthenticationFilter constructor with AuthenticationManager");
//    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String authType = request.getParameter("authType");
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        String tenantId = request.getParameter("tenantId");
        log.info("#### attemptAuthentication : {} / {} / {}", username, password, tenantId);
        CustomUserDetails customUserDetails = (CustomUserDetails) User
            .builder()
            .username(username)
            .password(password)
            .authorities(Role.USER.getAuthority())
            .build();
        customUserDetails.setAuthType(authType);
        customUserDetails.setTenantId(tenantId);

        UsernamePasswordAuthenticationToken authRequest =
            new UsernamePasswordAuthenticationToken(customUserDetails.getUsername(), customUserDetails.getPassword());
        authRequest.setDetails(customUserDetails);

        return this.getAuthenticationManager().authenticate(authRequest);
    }
}