package portal.config;

import java.util.List;
import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ApplicationConfig {

	@Bean("portalProperties")
    @ConfigurationProperties(prefix="portal")
    public PortalProperties portalProperties() {
		log.info("#### portalProperties Bean Create");
		return new PortalProperties();
    }

	@Setter
	@Getter
	public static class PortalProperties {
		private String name;
		private String version;
		private String storageMode;
		private Properties datasource;
		private List<String> publicPaths;
		private IpBlock ipBlock;
	}
	
	@Getter
	@Setter
	public static class IpBlock {
		private Boolean enable;
		private List<String> blacklist;
		private List<String> whitelist;
	}
	
}
