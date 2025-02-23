//package portal.config;
//
//import lombok.Data;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//
//@Data
//public class PrincipalDetails extends User {
//
//    private User user;
//
//    // 일반 로그인
//    public PrincipalDetails(User user) {
//        this.user = user;
//    }
//
//    // 해당 User의 권한을 리턴하는 곳!!
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//
//        Collection<GrantedAuthority> collect = new ArrayList<>();
//
//        collect.add(new GrantedAuthority() {
//            @Override
//            public String getAuthority() {
//                return user.getRole();
//            }
//        });
//
//        return collect;
//    }
//
//    @Override
//    public String getPassword() {
//        return user.getPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return user.getUsername();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}