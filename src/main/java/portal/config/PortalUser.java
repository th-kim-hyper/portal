package portal.config;

import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PortalUser {
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
	private Date createDt;
	private Date createId;
	private Date updateDt;
	private Date updateId;
}
