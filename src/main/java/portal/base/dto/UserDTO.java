package portal.base.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
	private String userId;
	private String password;
	private String name;
	private String phone;
	private String email;
	private String division;
}
