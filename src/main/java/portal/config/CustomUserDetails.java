package portal.config;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import portal.base.dto.UserDTO;

import java.util.Collection;

@Getter
@Setter
public class CustomUserDetails extends User {

    private UserDTO userDto;
    private String clientIp;
    private String authType;
    private String tenantId;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

}