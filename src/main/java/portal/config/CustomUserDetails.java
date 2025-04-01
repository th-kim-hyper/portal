package portal.config;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import portal.base.dto.UserDTO;

import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {

    @Getter(AccessLevel.NONE)
    private User user;
    private UserDTO userDto;
    private String clientIp;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    public CustomUserDetails(User user, UserDTO userDto) {
        this.user = user;
        this.userDto = userDto;
        this.clientIp = null;
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = user.getAuthorities();
        this.accountNonExpired = user.isAccountNonExpired();
        this.accountNonLocked = user.isAccountNonLocked();
        this.credentialsNonExpired = user.isCredentialsNonExpired();
        this.enabled = user.isEnabled();
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

}