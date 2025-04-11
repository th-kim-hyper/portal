package portal.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import portal.base.MailplugService;
import portal.base.RpaService;
import portal.base.dto.UserDTO;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final MailplugService mailplugService;
    private final RpaService rpaservice;
    private Function<Authentication, Authentication> authenticationFunction = null;

    public void SetAuthenticationFunction(Function<Authentication, Authentication> authenticationFunction) {
        this.authenticationFunction = authenticationFunction;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if (authenticationFunction != null) {
            return authenticationFunction.apply(authentication);
        }

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        String authority = (username.equals("admin") ? Role.ADMIN.getAuthority() : Role.USER.getAuthority());

        log.info("#### authenticate : {} / {} / {}", username, password, authority);

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getDetails();
        log.info("#### customUserDetails : {}", customUserDetails);

        String authType = customUserDetails.getAuthType();
        log.info("#### authType : {}", authType);

        if ("rpa".equals(authType)) {
//            String tenantId = customUserDetails.getTenantId();
            try {
                customUserDetails = rpaLogin(customUserDetails);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                customUserDetails = mailplugLogin(customUserDetails);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }

//        try {
//            String json = mailplugService.mailPlugLogin(username, password);
//            log.info("#### json : {}", json);
//
//            if (json == null || json.isEmpty()) {
//                throw new BadCredentialsException("로그인 아이디/비밀번호 가 올바르지 않습니다.");
//            }
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode jsonNode = objectMapper.readTree(json);
//            String email = jsonNode.get("emailAddress").asText();
//            String division = jsonNode.get("organization").asText();
//            JsonNode contact = jsonNode.get("contact");
//            String phone = contact.get("phone").asText();
//            String name = jsonNode.get("displayName").asText();
//            UserDTO userDTO = UserDTO.builder()
//                .userId(username)
//                .name(name)
//                .password(password)
//                .email(email)
//                .phone(phone)
//                .division(division)
//                .build();
//            customUserDetails.setUserDto(userDTO);
//        } catch (IOException | URISyntaxException e) {
//            throw new RuntimeException(e);
//        }

        return new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
    }

    private CustomUserDetails mailplugLogin(CustomUserDetails customUserDetails) throws IOException, URISyntaxException {
        String username = customUserDetails.getUsername();
        String password = customUserDetails.getPassword();
        String json = mailplugService.mailPlugLogin(username, password);
        log.info("#### json : {}", json);

        if (json == null || json.isEmpty()) {
            throw new BadCredentialsException("로그인 아이디/비밀번호 가 올바르지 않습니다.");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        String email = jsonNode.get("emailAddress").asText();
        String division = jsonNode.get("organization").asText();
        JsonNode contact = jsonNode.get("contact");
        String phone = contact.get("phone").asText();
        String name = jsonNode.get("displayName").asText();
        UserDTO userDTO = UserDTO.builder()
            .userId(username)
            .name(name)
            .password(password)
            .email(email)
            .phone(phone)
            .division(division)
            .build();
        customUserDetails.setUserDto(userDTO);
        return customUserDetails;
    }

    private CustomUserDetails rpaLogin(CustomUserDetails customUserDetails) throws IOException, URISyntaxException {
        String username = customUserDetails.getUsername();
        String password = customUserDetails.getPassword();
        String tenantId = customUserDetails.getTenantId();
        String json = rpaservice.rpaAdminLogin(username, password, tenantId);
        log.info("#### json : {}", json);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        String email = jsonNode.get("id").asText() + "@hyperinfo.co.kr";
        String division = jsonNode.get("deptSq").asText();
        String name = jsonNode.get("username").asText();

        UserDTO userDTO = UserDTO.builder()
            .userId(username)
            .name(name)
            .password(password)
            .email(email)
            .division(division)
            .build();

        customUserDetails.setUserDto(userDTO);
        return customUserDetails;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        log.info("#### supports : {}", authentication);
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}