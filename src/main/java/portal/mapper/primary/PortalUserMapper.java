package portal.mapper.primary;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PortalUserMapper {

	public String selectUserById(String userId);
	
	public long updateUser(String userId, String userName);
	
	public long updateT(long tid);

	public long insertT(long tid, String nm);
	
}
