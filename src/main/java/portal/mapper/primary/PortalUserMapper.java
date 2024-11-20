package portal.mapper.primary;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import portal.base.dto.PortalUserDTO;

@Mapper
public interface PortalUserMapper {

	public List<PortalUserDTO> findAllUser();
	
	public PortalUserDTO selectUserById(String userId);
	
	public long updateUser(String userId, String userName);
	
	public long updateT(long tid);

	public long insertT(long tid, String nm);
	
}
