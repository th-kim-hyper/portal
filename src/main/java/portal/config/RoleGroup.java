package portal.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum RoleGroup {

    USER_GROUP("사용자 그룹", Role.USER, Role.OPER, Role.DEV, Role.TEST, Role.ADMIN),
    API_GROUP("API 그룹", Role.API_USER, Role.API_OPER, Role.DEV, Role.TEST, Role.ADMIN),
    OPER_GROUP("운영 그룹", Role.OPER, Role.API_OPER, Role.DEV, Role.TEST, Role.ADMIN),
    DEV_GROUP("개발 그룹", Role.DEV, Role.TEST, Role.ADMIN),
    ADMIN_GROUP("관리자 그룹", Role.ADMIN);

    private final String title;
    private final Set<Role> roles;

    RoleGroup(String title, Role... roles) {
        this.title = title;
        this.roles = new HashSet<>(Arrays.asList(roles));
    }

    public boolean contains(Role role) {
        return roles.contains(role);
    }

    public static RoleGroup getByRole(Role role) {
        return Arrays.stream(RoleGroup.values())
            .filter(group -> group.contains(role))
            .findFirst()
            .orElse(null);
    }

    public String[] getAuthorities() {
        return roles.stream()
            .map(Role::getAuthority)
            .toArray(String[]::new);
    }

}