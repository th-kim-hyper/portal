package portal.base;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import portal.config.ApplicationConfig;

@Slf4j
@RequiredArgsConstructor
@Service
public class BaseService {
	
	@Value("${application.version}")
	private String version;
	
	private final ApplicationConfig applicationConfig;
	
	public String getVersion() {
		return this.version;
	}
	
	public Map<String, Object> getProps() {
		return applicationConfig.propertyMap();
	}

	public void printProps(Map<String, Object> props) {
		log.info("#### prop count : {}", props.size());
		for (Map.Entry<String, Object> entry : props.entrySet()) {
			String key = entry.getKey();
			String val = entry.getValue().toString();
			log.info("#### 키 : {}, 값 : {}", key, val);
		}
	}
}
