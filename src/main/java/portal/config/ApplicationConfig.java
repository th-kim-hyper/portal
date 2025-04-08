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

	@Getter
	@Setter
	public static class PortalProperties {
		private String name;
		private String version;
		private String storageMode;
		private Properties datasource;
		private Ext ext;
		private List<String> publicPaths;
		private Boolean ipBlockEnabled;
		private List<String> ipBlacklist;
		private List<String> ipWhitelist;
	}
	
	@Getter
	@Setter
	public static class Ext {
		private Boolean enable;
	}
	
}
