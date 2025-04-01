package portal.config;

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
        String authority = (username.equals("admin") ? Role.ADMIN.getAuthority() : Role.API_USER.getAuthority());

        log.info("#### authenticate : {} / {} / {}", username, password, authority);

        User user = (User) User
            .builder()
            .username(username)
            .password(password)
            .authorities(authority)
            .build();

//        UserDTO userDto = UserDTO
//            .builder()
//            .userId(user.getUsername())
//            .email("chatbot@hyperinfo.co.kr")
//            .phone("01012345678")
//            .build();

        // TODO: Implement your own logic for authentication
        log.warn("#### Implement your own logic for authentication");

        UserDTO userDto = null;
        try {
            username = "th.kim";
            password = "!G!493o18!";
            userDto = jsoupService.mailPlugLogin(username, password);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CustomUserDetails customUserDetails = new CustomUserDetails(user, userDto);

        return new UsernamePasswordAuthenticationToken(customUserDetails, null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        log.info("#### supports : {}", authentication);
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}