package portal.base;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import portal.mapper.primary.PortalUserMapper;
import portal.mapper.rpa.RpaUserMapper;

@Repository
@RequiredArgsConstructor
public class BaseRepository {

	private final PortalUserMapper portalUserMapper;
	@Nullable
	private final RpaUserMapper rpaUserMapper;
	
	public String getUser(String userId) {
		return portalUserMapper.selectUserById(userId);
	}
	
	public String getHyperUser(String userId) {
		if(rpaUserMapper != null) {
			return rpaUserMapper.selectUserById(userId);	
		}else {
			return "RPA 연결 안됨";
		}		
	}
	
	@Transactional
	public long exec() {
		portalUserMapper.updateUser("chatbot","ChatBot");
		long result = portalUserMapper.updateT(1);
		result = portalUserMapper.insertT(1, "test");
		return result;
	}
	
}
