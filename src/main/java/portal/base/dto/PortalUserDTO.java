package portal.base.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PortalUserDTO {

	private String userId;
	private String userName;
	private String email;
	private String orgId;
	private String deptId;
	private String grpId;
	private Date recentLoginId;
	private String recentLoginIp;
	private Date passwordExpireDate;
	private Date accountExpireDate;
	private Boolean isDel;
	private Timestamp createDt;
	private String createId;
	private Date updateDt;
	private String updateId;
	
}
