package portal.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import portal.base.dto.PortalUserDTO;
import portal.config.ApplicationConfig;
import portal.config.ApplicationConfig.PortalProperties;

@Slf4j
@RequiredArgsConstructor
@Service
public class BaseService {
	
	private final ApplicationConfig applicationConfig;
	private final BaseRepository baseRepository;
	
	public PortalProperties getPortalProperties() {
		return applicationConfig.portalProperties();
	}

	public List<PortalUserDTO> findAllUser() {
		return baseRepository.findAllUser();
	}
	
	public PortalUserDTO getPortalUser(String userId) {
		return baseRepository.findByUserId(userId);
	}

	public List<String> getWhitelist() {
		return applicationConfig.portalProperties().getWhitelist();
	}

}
