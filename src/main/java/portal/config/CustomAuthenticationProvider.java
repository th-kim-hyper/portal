package portal.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import portal.base.JsoupService;
import portal.base.dto.UserDTO;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final JsoupService jsoupService;
    private Function<Authentication, Authentication> authenticationFunction = null;

    public void SetAuthenticationFunction(Function<Authentication, Authentication> authenticationFunction) {
        this.authenticationFunction = authenticationFunction;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if(authenticationFunction != null) {
            return authenticationFunction.apply(authentication);
        }

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        String authority = (username.equals("admin") ? Role.ADMIN.getAuthority() : Role.USER.getAuthority());

        log.info("#### authenticate : {} / {} / {}", username, password, authority);

        User user = (User) User
            .builder()
            .username(username)
            .password(password)
            .authorities(authority)
            .build();
        UserDTO userDTO = null;

        try {
            String json = jsoupService.mailPlugLogin(username, password);
            log.info("#### json : {}", json);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(json);
            String email = jsonNode.get("emailAddress").asText();
            String division = jsonNode.get("organization").asText();
            JsonNode contact = jsonNode.get("contact");
            String phone = contact.get("phone").asText();
            String name = jsonNode.get("displayName").asText();
            userDTO = UserDTO.builder()
                .userId(username)
                .name(name)
                .password(password)
                .email(email)
                .phone(phone)
                .division(division)
                .build();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        CustomUserDetails customUserDetails = new CustomUserDetails(user, userDTO);
        return new UsernamePasswordAuthenticationToken(customUserDetails, null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        log.info("#### supports : {}", authentication);
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}