package portal.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER("ROLE_USER", "사용자"),
    OPER("ROLE_OPER", "운영자"),
    API_USER("ROLE_API_USER", "API 사용자"),
    API_OPER("ROLE_API_OPER", "API 운영자"),
    ADMIN("ROLE_ADMIN", "시스템 관리자");

    private final String authority;
    private final String title;

}
