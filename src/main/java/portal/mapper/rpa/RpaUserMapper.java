package portal.mapper.rpa;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RpaUserMapper {

	public String selectUserById(String userId);
	
}
